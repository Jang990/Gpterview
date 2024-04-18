package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.InterviewCacheRepository;
import com.mock.interview.interviewconversationpair.application.LastConversationHelper;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewPhase;
import com.mock.interview.interviewquestion.infra.ai.progress.TraceResult;
import com.mock.interview.interviewquestion.infra.recommend.CurrentConversationConvertor;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.progress.CurrentTopicTracker;
import com.mock.interview.interviewquestion.infra.recommend.QuestionRankingService;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** 변경이 있을 때 같이 볼 것
 * @see com.mock.interview.interviewquestion.infra.cache.RecommendationCacheAspect
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRecommenderImpl implements QuestionRecommender {

    private final InterviewCacheRepository interviewCache;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final QuestionRankingService recommender;
    private final KoreaStringAnalyzer stringAnalyzer;
    private final CurrentTopicTracker topicTracker;

    private final int RECOMMENDED_QUESTION_SIZE = 30;
    private final int TOP_3 = 3;

    @Override
    public List<InterviewQuestion> recommend(int recommendationSize, RecommendationTarget target) {
        InterviewInfo interview = interviewCache.findProgressingInterviewInfo(target.interviewId());
        TraceResult interviewTraceResult = topicTracker.trace(interview);
        List<QuestionMetaData> questionForRecommend = findRelatedRandomQuestions(interview, interviewTraceResult.progress().phase(),RECOMMENDED_QUESTION_SIZE);

        try {
            CurrentConversation currentConversation = CurrentConversationConvertor
                    .create(conversationPairRepository, stringAnalyzer,
                            target.interviewId(), interview, interviewTraceResult.currentTopic());
            List<Long> result = recommender
                    .recommendTechQuestion(recommendationSize, currentConversation, questionForRecommend);
            return result.stream().map(questionRepository::findById).map(op -> op.orElseThrow(InterviewQuestionNotFoundException::new)).toList();
        } catch (NotEnoughQuestion e) {
            log.warn("추천 기능 예외 발생", e);
            throw new IllegalArgumentException(e);
        }
    }

    private List<QuestionMetaData> findRelatedRandomQuestions(InterviewInfo interview, InterviewPhase phase, int size) {
        return questionRepository.findRandomQuestion(interview.profile().category(), PageRequest.of(0, size))
                .stream().map(this::convertQuestion).toList();
        // TODO: 페이즈별로 다른 쿼리가 필요함.
//        return switch (phase) {
//            case TECHNICAL -> null;
//            case EXPERIENCE -> null;
//            case PERSONAL -> null;
//        };
    }

    @Override
    public Top3Question recommendTop3(RecommendationTarget target) {
        List<InterviewQuestion> recommended = recommend(TOP_3, target);
        return new Top3Question(recommended.stream().map(InterviewQuestion::getId).toList());
    }

    @Override
    public Top3Question retryRecommendation(RecommendationTarget target) {
        // 현재 저장된 캐시를 만료하고 새롭게 저장 - AOP 처리
        return recommendTop3(target);
    }

    private QuestionMetaData convertQuestion(InterviewQuestion question) {
        return new QuestionMetaData(
                question.getId(),
                (question.getParentQuestion() == null) ? null : question.getParentQuestion().getId(),
                (question.getPosition() == null) ? null : question.getPosition().getName(),
                convertTechLink(question),
                question.getQuestionToken().getResult(),
                question.getLikes()
        );
    }

    private List<String> convertTechLink(InterviewQuestion q) {
        return q.getTechLink().stream().map(QuestionTechLink::getTechnicalSubjects).map(TechnicalSubjects::getName).toList();
    }
}
