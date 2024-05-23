package com.mock.interview.interviewquestion.infra;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewquestion.infra.recommend.CurrentConversationConvertor;
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
    private final QuestionRankingService recommender;

    private final int TOP_3 = 3;

    @Override
    public List<Long> recommend(
            int recommendationSize,
            CurrentConversation currentConversation,
            List<QuestionMetaData> relatedQuestions
    ) throws NotEnoughQuestion {
        return recommender
                .recommendTechQuestion(recommendationSize, currentConversation, relatedQuestions);
    }

    @Override
    public Top3Question recommendTop3(
            CurrentConversation currentConversation, List<QuestionMetaData> relatedQuestions
    ) throws NotEnoughQuestion {
        return new Top3Question(recommend(TOP_3, currentConversation, relatedQuestions));
    }

    @Override
    public Top3Question retryRecommendation(
            CurrentConversation currentConversation, List<QuestionMetaData> relatedQuestions
    ) throws NotEnoughQuestion {
        // 현재 저장된 캐시를 만료하고 새롭게 저장 - AOP 처리
        return new Top3Question(recommend(TOP_3, currentConversation, relatedQuestions));
    }
}
