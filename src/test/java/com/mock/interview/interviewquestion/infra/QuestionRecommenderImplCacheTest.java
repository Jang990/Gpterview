package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class QuestionRecommenderImplCacheTest {
    @Autowired QuestionRecommenderImpl recommender;

//    @Test
    @DisplayName("캐싱 테스트")
    void test1() {
        recommender.recommendTop3(new RecommendationTarget(144, 55));
        recommender.recommendTop3(new RecommendationTarget(144, 55));
    }

//    @Test
    @DisplayName("새로운 캐싱 테스트")
    void test2() {
        recommender.retryRecommendation(new RecommendationTarget(144, 55));
        recommender.retryRecommendation(new RecommendationTarget(144, 55));
    }

}