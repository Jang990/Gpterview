package com.mock.interview.conversation.infrastructure.interview.dto;

import com.mock.interview.interview.presentation.dto.InterviewType;

public record InterviewConfig(InterviewType interviewType, int durationMinutes) {
}
