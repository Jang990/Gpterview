package com.mock.interview.interviewquestion.infra.cache;

import com.mock.interview.interviewquestion.presentation.dto.recommendation.RecommendationTarget;
import com.mock.interview.interviewquestion.presentation.dto.recommendation.Top3Question;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@SpringBootTest
class RecommendedQuestionCacheRepositoryTest {

    private final int pairId = 123;
    private final int interviewId = 10;
    @Autowired RecommendedQuestionCacheRepository repository;

//    @Test
    void test() {
        Top3Question top3Question = new Top3Question(List.of(14L, 2L, 31L));
        repository.save(new RecommendationTarget(interviewId, pairId), top3Question);
    }

//    @Test
    void test2() {
        Top3Question cache = repository.find(new RecommendationTarget(interviewId, pairId));
        System.out.println(cache);
    }

}