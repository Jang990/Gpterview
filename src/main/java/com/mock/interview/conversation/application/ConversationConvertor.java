package com.mock.interview.conversation.application;

import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;

public class ConversationConvertor {
    public static Message convert(InterviewConversation conversation) {
        return new Message(conversation.getInterviewConversationType().toString(), conversation.getContent());
    }
}
