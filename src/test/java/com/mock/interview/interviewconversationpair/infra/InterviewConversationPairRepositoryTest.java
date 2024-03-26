package com.mock.interview.interviewconversationpair.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

//@SpringBootTest
class InterviewConversationPairRepositoryTest {

    @Autowired InterviewConversationPairRepository repository;

//    @Test
    void test1() {
        System.out.println(repository.findLastCompletedConversation(0L, PageRequest.of(0, 1)));
    }

}