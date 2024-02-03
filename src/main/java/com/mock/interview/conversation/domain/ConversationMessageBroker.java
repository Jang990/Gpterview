package com.mock.interview.conversation.domain;

import com.mock.interview.conversation.presentation.dto.MessageDto;

public interface ConversationMessageBroker {
    void publish(long interviewId, MessageDto message);
}
