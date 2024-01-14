package com.mock.interview.conversation.infrastructure.interview.dto;

import com.mock.interview.candidate.presentation.dto.InterviewType;

import java.time.LocalDateTime;

public record InterviewConfig(InterviewType interviewType, LocalDateTime end) {
}
