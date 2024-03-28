package com.mock.interview.interview.domain;

import com.mock.interview.interview.infra.lock.proceeding.LockableCustomInterviewEvent;

public record InterviewContinuedEvent(long interviewId) implements LockableCustomInterviewEvent {
    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
