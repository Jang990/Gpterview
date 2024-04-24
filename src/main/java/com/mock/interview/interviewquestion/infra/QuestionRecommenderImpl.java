package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.application.helper.QuestionConvertor;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.infra.recommend.CurrentConversationConvertor;
import com.mock.interview.interviewquestion.infra.recommend.QuestionMetaDataConvertor;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.interviewquestion.domain.QuestionRecommender;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.InterviewProgressTraceService;
import com.mock.interview.interviewquestion.infra.recommend.QuestionRankingService;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final RandomQuestionRepository randomQuestionRepository;
    private final InterviewConversationPairRepository conversationPairRepository;
    private final QuestionRankingService recommender;
    private final KoreaStringAnalyzer stringAnalyzer;
    private final InterviewProgressTraceService interviewProgressTraceService;

    private final int RECOMMENDED_QUESTION_SIZE = 30;
    private final int TOP_3 = 3;

    @Override
    public List<InterviewQuestion> recommend(int recommendationSize, RecommendationTarget target) {
        InterviewInfo interview = interviewCache.findProgressingInterviewInfo(target.interviewId());
        InterviewProgress interviewInterviewProgress = interviewProgressTraceService.trace(interview);
        List<InterviewQuestion> relatedQuestions = findRelatedRandomQuestions(interviewInterviewProgress, RECOMMENDED_QUESTION_SIZE);
        List<QuestionMetaData> questionForRecommend = QuestionMetaDataConvertor.convert(relatedQuestions);

        try {
            CurrentConversation currentConversation = CurrentConversationConvertor
                    .create(conversationPairRepository, stringAnalyzer,
                            target.interviewId(), interview, interviewInterviewProgress.getTopicContent());
            List<Long> result = recommender
                    .recommendTechQuestion(recommendationSize, currentConversation, questionForRecommend);
            return result.stream().map(questionRepository::findById).map(op -> op.orElseThrow(InterviewQuestionNotFoundException::new)).toList();
        } catch (NotEnoughQuestion e) {
            log.warn("추천 기능 예외 발생", e);
            throw new IllegalArgumentException(e);
        }
    }

    private List<InterviewQuestion> findRelatedRandomQuestions(InterviewProgress interviewProgress, int size) {
        final PageRequest pageable = PageRequest.of(0, size);
        QuestionType type = QuestionConvertor.convert(interviewProgress.phase());
        return switch (interviewProgress.phase()) {
            case TECHNICAL -> randomQuestionRepository.findTechQuestion(type, interviewProgress.getTopicId(), pageable);
            case EXPERIENCE -> randomQuestionRepository.findExperienceQuestion(type, interviewProgress.getTopicId(), pageable);
            case PERSONAL -> randomQuestionRepository.findPersonalQuestion(type, pageable);
        };
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
}
