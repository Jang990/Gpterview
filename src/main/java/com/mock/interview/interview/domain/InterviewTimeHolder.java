package com.mock.interview.interview.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterviewTimeHolder {
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
