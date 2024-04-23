package com.mock.interview.interviewquestion.infra.gpt.dto;

import com.mock.interview.interviewquestion.infra.gpt.prompt.AiPrompt;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class InterviewAIRequest {
    private final List<Message> history;
    private final AiPrompt aiPrompt;

    public InterviewAIRequest(List<Message> history, AiPrompt aiPrompt) {
        this.history = history;
        this.aiPrompt = aiPrompt;
    }
}
