package com.mock.interview.interviewconversationpair.domain;

import com.mock.interview.interview.infra.lock.response.LockableInterviewEvent;

public record AiQuestionRecommendedEvent(long interviewId, long pairId) implements LockableInterviewEvent {
    @Override
    public Long getInterviewId() {
        return interviewId();
    }
}
