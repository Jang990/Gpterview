package com.mock.interview.presentaion.dto;

import lombok.Getter;

@Getter
public enum InterviewRole {
    SYSTEM("system"), USER("user"), INTERVIEWER("interviewer");
    private final String name;
    InterviewRole(String name) {
        this.name = name;
    }
}
