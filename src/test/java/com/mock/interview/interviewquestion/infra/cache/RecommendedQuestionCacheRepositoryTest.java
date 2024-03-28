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
    @Autowired RecommendedQuestionCacheRepository repository;

//    @Test
    void test() {
        List<Long> list1 = List.of(14L, 2L, 31L);
        repository.save(interviewId, pairId, list1);
    }

//    @Test
    void test2() {
        List<Long> list = repository.find(interviewId, pairId);
        System.out.println(list);
    }

}