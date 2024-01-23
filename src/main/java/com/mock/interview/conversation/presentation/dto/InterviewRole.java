package com.mock.interview.conversation.presentation.dto;

import lombok.Getter;

@Getter
public enum InterviewRole {
    SYSTEM("system"), USER("user"), AI("AI");
    private final String name;
    InterviewRole(String name) {
        this.name = name;
    }
}
