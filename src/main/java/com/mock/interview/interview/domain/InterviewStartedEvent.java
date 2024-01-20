package com.mock.interview.interview.domain;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.user.domain.model.Users;

import java.time.LocalDateTime;

public record InterviewStartedEvent(long interviewId) {
}
