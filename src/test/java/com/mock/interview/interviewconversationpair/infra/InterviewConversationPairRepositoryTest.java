package com.mock.interview.interviewconversationpair.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@SpringBootTest
class InterviewConversationPairRepositoryTest {

    @Autowired InterviewConversationPairRepository repository;

    final PageRequest LAST = PageRequest.of(0, 1);

//    @Test
    void test1() {
        System.out.println(repository.findLastCompletedConversation(0L, LAST));
    }

//    @Test
    void test2() {
        System.out.println(repository.findWithInterviewUser(31, 125, 7).isPresent());
    }

//    @Test
    void test3() {
        System.out.println(repository.findByIdWithInterviewId(67, 154).isPresent());
    }

//    @Test
    void test4() {
        System.out.println(repository.findLastCompletedConversation(7, 125, LAST).get(0).getId());
    }

//    @Test
    void test5() {
        List<Long> ids = repository.findAppearedQuestionIds(171L);
        System.out.println(ids);
    }
}