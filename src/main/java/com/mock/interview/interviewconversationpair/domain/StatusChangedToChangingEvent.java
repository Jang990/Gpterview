package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.infra.lock.proceeding.LockableInterviewEvent;

public record StatusChangedToChangingEvent(long interviewId, long pairId) implements LockableInterviewEvent {
    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
