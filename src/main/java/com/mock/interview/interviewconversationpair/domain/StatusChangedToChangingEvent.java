package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.infra.lock.proceeding.LockableCustomInterviewEvent;

public record StatusChangedToChangingEvent(long interviewId, long pairId) implements LockableCustomInterviewEvent {
    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
