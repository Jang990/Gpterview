package com.mock.interview.conversation.domain;

import com.mock.interview.interview.infrastructure.lock.proceeding.LockableCustomInterviewEvent;

public record ChangeTopicEvent(long interviewId) implements LockableCustomInterviewEvent {
    @Override
    public Long getInterviewId() {
        return this.interviewId();
    }
}
