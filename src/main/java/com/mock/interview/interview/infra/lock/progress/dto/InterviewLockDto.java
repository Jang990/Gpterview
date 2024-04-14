package com.mock.interview.interview.infra.lock.progress.dto;

import com.mock.interview.interview.infra.lock.progress.InterviewProgressLockable;

public record InterviewLockDto(Long interviewId, Long userId) implements InterviewProgressLockable {
    @Override
    public Long getInterviewId() {
        return interviewId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }
}
