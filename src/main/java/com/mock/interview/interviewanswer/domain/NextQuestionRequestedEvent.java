package com.mock.interview.interviewanswer.domain;

import com.mock.interview.interview.infrastructure.lock.proceeding.LockableCustomInterviewEvent;

public record NextQuestionRequestedEvent(long interviewId) implements LockableCustomInterviewEvent {

    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
