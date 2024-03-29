package com.mock.interview.interviewconversationpair.infra;

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
        Slice<ConversationContentDto> interviewConversations = repository
                .findInterviewConversations(156, 7, PageRequest.of(0, 5));
        List<ConversationContentDto> content = interviewConversations.getContent();
        System.out.println(content.size());
        for (ConversationContentDto conversationContentDto : content) {
            System.out.println("interviewConversationPairDto = " + conversationContentDto);
        }
    }

}