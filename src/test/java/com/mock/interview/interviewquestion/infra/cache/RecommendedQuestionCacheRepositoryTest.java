package com.mock.interview.interviewquestion.infra.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class RecommendedQuestionCacheRepositoryTest {

    private final int pairId = 123;
    private final int interviewId = 10;
    ;
    @Autowired RecommendedQuestionCacheRepository repository;

//    @Test
    void test() {
        List<Long> list1 = List.of(1L, 2L, 3L);
        List<Long> list2 = List.of(162L, 726L, 922L);
        List<Long> list3 = List.of(100L, 43L, 12L);
        repository.save(interviewId, pairId, List.of(list1, list2, list3));
    }

//    @Test
    void test2() {
        List<Long> list = repository.find(interviewId, pairId);
        System.out.println(list);
    }

//    @Test
    void test3() {
        repository.expiredNow(interviewId, pairId);
        System.out.println(repository.find(interviewId, pairId));
    }

}