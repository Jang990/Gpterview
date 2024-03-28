package com.mock.interview.interview.domain;

import com.mock.interview.interview.infra.lock.response.LockableInterviewEvent;

public record InterviewContinuedEvent(long interviewId) implements LockableInterviewEvent {
    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
