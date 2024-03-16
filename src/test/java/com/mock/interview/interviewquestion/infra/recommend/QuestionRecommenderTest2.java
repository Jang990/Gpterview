package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class QuestionRecommenderTest2 {

    @Autowired
    QuestionRecommender recommender;


//    @Test
    void test() throws NotEnoughQuestion {
        CurrentQuestion user = new CurrentQuestion(1l, Arrays.asList("Spring", "MVC", "대해", "아는대로", "설명", "해보세요"), "Spring Boot", "백엔드"); // Spring MVC에 대해 아는대로 설명해보세요.
        List<Long> longs = recommender.recommendTechQuestion(3, user, SimpleRecommendScoreCalculatorTest.createSimpleTestData());
        System.out.println(longs);
    }
}