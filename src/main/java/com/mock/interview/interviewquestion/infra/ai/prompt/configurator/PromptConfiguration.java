package com.mock.interview.interviewquestion.infra.ai.prompt.configurator;

import lombok.Getter;

import java.util.List;

@Getter
public class PromptConfiguration {
    private final String promptTemplate;
    private final String category;
    private final String field;
    private final List<String> skills;
    private final List<String> experience;

    /**
     * @param promptTemplate $_user_는 지원자. $_interviewer_는 면접관입니다. $_user_의 기술은 $_skills_입니다.
     * @param category     IT, 회계 등등
     * @param field          백엔드, 프론트엔드, 안드로이드, IOS 등등
     * @param skills         Spring, Java 등등
     * @param experience     ~경험이 있습니다.
     */
    PromptConfiguration(String promptTemplate, String category, String field, List<String> skills, List<String> experience) {
        this.promptTemplate = promptTemplate;
        this.category = category;
        this.field = field;
        this.skills = skills;
        this.experience = experience;
    }
}
