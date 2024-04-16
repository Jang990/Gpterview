package com.mock.interview.interviewconversationpair.domain.event;

public record ConversationResetEvent(long interviewId, long conversationId, String resetMessage) {
}
