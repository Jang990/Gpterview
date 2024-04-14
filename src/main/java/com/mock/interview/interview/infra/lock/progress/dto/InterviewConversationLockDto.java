package com.mock.interview.interview.infra.lock.progress.dto;

import com.mock.interview.interview.infra.lock.progress.InterviewProgressLockable;

public record InterviewConversationLockDto(Long userId, Long interviewId, Long conversationId) implements InterviewProgressLockable {
    @Override
    public Long getInterviewId() {
        return interviewId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }
}
