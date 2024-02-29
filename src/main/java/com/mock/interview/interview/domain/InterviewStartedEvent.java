package com.mock.interview.interview.domain;

import com.mock.interview.interviewquestion.infra.lock.LockableCustomInterviewEvent;

public record InterviewStartedEvent(long interviewId) implements LockableCustomInterviewEvent {
    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
