package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.RecommendationTarget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class QuestionRecommenderImplCacheTest {
    @Autowired QuestionRecommenderImpl recommender;

//    @Test
    @DisplayName("캐싱 테스트")
    void test() {
        recommender.recommendTop3(new RecommendationTarget(144, 55));
        recommender.recommendTop3(new RecommendationTarget(144, 55));
    }

}