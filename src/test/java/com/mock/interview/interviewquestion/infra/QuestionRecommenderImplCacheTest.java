package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

//@SpringBootTest
class QuestionRecommenderImplCacheTest {
    @Autowired QuestionRecommenderImpl recommender;

//    @Test
    @DisplayName("캐싱 테스트")
    void test1() throws NotEnoughQuestion {
//        recommender.recommendTop3(new RecommendationTarget(144, 55), Collections.emptyList());
//        recommender.recommendTop3(new RecommendationTarget(144, 55), Collections.emptyList());
    }

//    @Test
    @DisplayName("새로운 캐싱 테스트")
    void test2() throws NotEnoughQuestion {
//        recommender.retryRecommendation(new RecommendationTarget(144, 55), Collections.emptyList());
//        recommender.retryRecommendation(new RecommendationTarget(144, 55), Collections.emptyList());
    }

}