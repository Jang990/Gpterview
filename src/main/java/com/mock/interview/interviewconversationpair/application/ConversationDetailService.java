package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationDetailService {
    private final InterviewConversationRepositoryForView conversationRepositoryForView;

    public ConversationContentDto findConversation(long userId, long interviewId, long conversationId) {
        ConversationContentDto conversation = conversationRepositoryForView.findConversation(userId, interviewId, conversationId);
        if(conversation == null)
            throw new InterviewConversationPairNotFoundException();
        return conversation;
    }
}
