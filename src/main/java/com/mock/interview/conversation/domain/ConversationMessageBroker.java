package com.mock.interview.conversation.domain;

import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.presentation.dto.MessageDto;

public interface ConversationMessageBroker {
    void publish(String interviewId, MessageDto message);
}
