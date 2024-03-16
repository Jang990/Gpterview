package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.calculator.SimpleRecommendScoreCalculatorTest;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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