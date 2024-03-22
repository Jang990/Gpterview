package com.mock.interview.interviewconversationpair.domain;

public interface ConversationMessageBroker {
    void publish(long interviewId, long conversationPairId, long questionId, String question);
}
