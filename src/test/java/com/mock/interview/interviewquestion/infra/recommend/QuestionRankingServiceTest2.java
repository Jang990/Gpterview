package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.calculator.RecommendScorerTest;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.interviewquestion.infra.recommend.exception.NotEnoughQuestion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

//@SpringBootTest
class QuestionRankingServiceTest2 {

    @Autowired
    QuestionRankingService rankingService;


//    @Test
    void test() throws NotEnoughQuestion {
        CurrentConversation user = new CurrentConversation(1l, Arrays.asList("Spring", "MVC", "대해", "아는대로", "설명", "해보세요"), "Spring Boot", "백엔드"); // Spring MVC에 대해 아는대로 설명해보세요.
        List<Long> longs = rankingService.recommendTechQuestion(3, user, RecommendScorerTest.createSimpleTestData());
        System.out.println(longs);
    }
}