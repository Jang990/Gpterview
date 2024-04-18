package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import lombok.Getter;

import java.util.List;

@Getter
public class PromptConfiguration {
    private final String promptTemplate;
    private final String category;
    private final String field;
    private final String skills;
    private final String experience;

    /**
     * @param promptTemplate $_user_는 지원자. $_interviewer_는 면접관입니다. $_user_의 기술은 $_skills_입니다.
     * @param elements 템플릿에 들어갈 정보
     */
    PromptConfiguration(String promptTemplate, PromptConfigElements elements) {
        this.promptTemplate = promptTemplate;
        this.category = elements.category().getName();
        this.field = elements.field() == null ? null : elements.field().getName();
        this.skills = elements.tech() == null ? null : elements.tech().getName();
        this.experience = elements.experience() == null ? null : elements.experience().getContent();
    }
}
