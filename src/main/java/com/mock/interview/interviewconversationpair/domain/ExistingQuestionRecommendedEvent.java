package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.infra.lock.proceeding.LockableInterviewEvent;

public record ExistingQuestionRecommendedEvent(long interviewId, long pairId) implements LockableInterviewEvent {
    @Override
    public Long getInterviewId() {
        return interviewId();
    }
}
