package com.mock.interview.interviewquestion.infra.ai.gpt;

import com.mock.interview.interview.infra.prompt.AiPrompt;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
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
