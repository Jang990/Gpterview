package com.mock.interview.interviewquestion.infra.interview.prompt;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AiPrompt {
    private final String prompt;
    protected AiPrompt(String prompt) {
        this.prompt = prompt;
    }
}