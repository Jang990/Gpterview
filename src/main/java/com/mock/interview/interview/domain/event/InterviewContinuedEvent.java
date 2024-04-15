package com.mock.interview.interview.domain.event;

import com.mock.interview.interview.infra.lock.response.LockableInterviewEvent;

public record InterviewContinuedEvent(long interviewId) {
}
