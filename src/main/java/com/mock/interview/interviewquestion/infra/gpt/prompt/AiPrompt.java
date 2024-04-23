package com.mock.interview.interviewquestion.infra.gpt.prompt;

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