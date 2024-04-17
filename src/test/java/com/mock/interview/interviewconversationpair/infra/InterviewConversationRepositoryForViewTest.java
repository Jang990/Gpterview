package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@SpringBootTest
class InterviewConversationRepositoryForViewTest {

    @Autowired InterviewConversationRepositoryForView repository;

//    @Test
//    @Transactional
    void myTest() {
        List<InterviewConversationPair> list = repository
                .findOrderedByCreatedAt(156, 7);
        for (InterviewConversationPair pair : list) {
            System.out.println("pair = " + pair.getId());
        }
    }

//    @Test
    void test2() {
        InterviewConversationPair conversation = repository.findConversation(7, 1, 1);
        System.out.println(conversation);
    }

}