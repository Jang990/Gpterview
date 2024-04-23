package com.mock.interview.interviewquestion.infra.gpt.prompt.config;

import lombok.Getter;

@Getter
public class PromptConfig {
    private final String promptTemplate;
    private final String category;
    private final String field;
    private final String topic;

    /**
     * @param promptTemplate $_user_는 지원자. $_interviewer_는 면접관입니다. $_user_의 기술은 $_topic_입니다.
     * @param elements 템플릿에 들어갈 정보
     */
    PromptConfig(String promptTemplate, PromptElements elements) {
        this.promptTemplate = promptTemplate;
        this.category = elements.category();
        this.field = elements.field();
        this.topic = elements.topic();
    }
}
