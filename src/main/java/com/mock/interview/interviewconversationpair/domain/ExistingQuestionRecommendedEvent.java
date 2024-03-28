package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.infra.lock.proceeding.LockableCustomInterviewEvent;

public record ExistingQuestionRecommendedEvent(long interviewId, long pairId) implements LockableCustomInterviewEvent {
    @Override
    public Long getInterviewId() {
        return interviewId();
    }
}
