package com.mock.interview.interviewconversationpair.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class InterviewConversationPairRepositoryTest {

    @Autowired InterviewConversationPairRepository repository;

//    @Test
    void test1() {
        System.out.println(repository.findLastCompletedConversation(0L, PageRequest.of(0, 1)));
    }

//    @Test
    void test2() {
        System.out.println(repository.findWithInterviewUser(31, 125, 7).isPresent());
    }

    @Test
    void test3() {
        System.out.println(repository.findByIdWithInterviewId(67, 154).isPresent());
    }

}