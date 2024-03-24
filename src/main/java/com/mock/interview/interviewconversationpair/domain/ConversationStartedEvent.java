package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.infrastructure.lock.proceeding.LockableCustomInterviewEvent;

public record ConversationStartedEvent(long interviewId, long pairId) implements LockableCustomInterviewEvent {
    @Override
    public Long getInterviewId() {
        return interviewId();
    }
}
