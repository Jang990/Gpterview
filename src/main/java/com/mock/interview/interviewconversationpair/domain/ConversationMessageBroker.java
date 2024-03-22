package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.presentation.dto.message.MessageDto;

public interface ConversationMessageBroker {
    void publish(long interviewId, long conversationPairId, MessageDto message);
}
