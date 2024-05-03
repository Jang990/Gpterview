package com.mock.interview.interviewquestion.presentation.dto;

import lombok.Getter;

@Getter
public enum QuestionTypeForView {
    TECHNICAL("기술", "fa-wrench"),
    PERSONALITY("인성", "fa-users"),
    EXPERIENCE("경험", "fa-book");

    private final String name;
    private final String iconImageClass;

    QuestionTypeForView(String name, String iconImageClass) {
        this.name = name;
        this.iconImageClass = iconImageClass;
    }
}
