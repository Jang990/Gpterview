package com.mock.interview.conversation.domain;

import com.mock.interview.conversation.infrastructure.interview.dto.Message;

public interface ConversationMessageBroker {
    void publish(String interviewId, Message message);
}
