package com.mock.interview.interview.presentation.dto;

import lombok.Getter;

@Getter
public enum InterviewRole {
    SYSTEM("system"), USER("user"), AI("AI");
    private final String name;
    InterviewRole(String name) {
        this.name = name;
    }
}
