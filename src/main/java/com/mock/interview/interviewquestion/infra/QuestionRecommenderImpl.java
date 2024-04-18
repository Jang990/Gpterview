package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.InterviewCacheForAiRequest;
import com.mock.interview.interviewconversationpair.application.LastConversationHelper;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
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

    private final InterviewCacheForAiRequest interviewCache;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final QuestionRankingService recommender;
    private final KoreaStringAnalyzer stringAnalyzer;
    private final CurrentTopicTracker topicTracker;

    private final int RECOMMENDED_QUESTION_COUNT = 30;
    private final int TOP_3 = 3;

    @Override
    public List<InterviewQuestion> recommend(int recommendationSize, RecommendationTarget target) {
        InterviewInfo interview = interviewCache.findProgressingInterviewInfo(target.interviewId());
        String currentInterviewTopic = topicTracker.trace(interview).topic();
        List<QuestionMetaData> questionForRecommend = questionRepository
                .findRandomQuestion(interview.profile().category(), PageRequest.of(0, RECOMMENDED_QUESTION_COUNT))
                .stream().map(this::convertQuestion).toList();

        CurrentConversation currentConversation = initCurrentConversation(target.interviewId(), interview, currentInterviewTopic);

        try {
            List<Long> result = recommender
                    .recommendTechQuestion(recommendationSize, currentConversation, questionForRecommend);
            return result.stream().map(questionRepository::findById).map(op -> op.orElseThrow(InterviewQuestionNotFoundException::new)).toList();
        } catch (NotEnoughQuestion e) {
            log.warn("추천 기능 예외 발생", e);
            throw new IllegalArgumentException(e);
        }
    }

    private CurrentConversation initCurrentConversation(long interviewId, InterviewInfo interview, String currentTopic) {
        Optional<InterviewConversationPair> optCurrentConversation = LastConversationHelper
                .findCurrentCompletedConversation(conversationPairRepository, interviewId);

        if(optCurrentConversation.isEmpty())
            return createEmptyConversation(interview, currentTopic);

        InterviewConversationPair currentConversation = optCurrentConversation.get();
        if(currentConversation.getQuestion() == null)
            return createEmptyConversation(interview, currentTopic);

        return new CurrentConversation(
                currentConversation.getAnswer().getId(),
                stringAnalyzer.extractNecessaryTokens(currentConversation.getAnswer().getAnswer()),
                currentTopic, interview.profile().field()
        );
    }

    private CurrentConversation createEmptyConversation(InterviewInfo interview, String topic) {
        return new CurrentConversation(null,null, topic, interview.profile().field());
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
