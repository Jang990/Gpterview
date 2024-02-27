package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class InterviewConversationRepositoryForViewTest {

    @Autowired InterviewConversationRepositoryForView repository;

//    @Test
//    @Transactional
    void myTest() {
        Slice<InterviewConversationPairDto> interviewConversations = repository
                .findInterviewConversations(106, 7, PageRequest.of(0, 5));
        List<InterviewConversationPairDto> content = interviewConversations.getContent();
        System.out.println(content.size());
        for (InterviewConversationPairDto interviewConversationPairDto : content) {
            System.out.println("interviewConversationPairDto = " + interviewConversationPairDto);
        }
    }

}