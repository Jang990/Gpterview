package com.mock.interview.interview.domain;

import com.mock.interview.user.domain.model.Users;

import java.time.LocalDateTime;

public interface ActiveInterviewFinder {
    boolean hasActiveInterview(Users users, LocalDateTime now);
}
