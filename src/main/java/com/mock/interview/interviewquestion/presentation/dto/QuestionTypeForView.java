package com.mock.interview.interviewquestion.presentation.dto;

import lombok.Getter;

@Getter
public enum QuestionTypeForView {
    TECHNICAL("기술"), PERSONALITY("인성"), EXPERIENCE("경험");
    private final String name;

    QuestionTypeForView(String name) {
        this.name = name;
    }
}
