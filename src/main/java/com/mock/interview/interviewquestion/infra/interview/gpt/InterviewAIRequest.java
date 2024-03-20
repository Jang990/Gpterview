package com.mock.interview.interviewquestion.infra.interview.gpt;

import com.mock.interview.interviewquestion.infra.interview.setting.AiPrompt;
import com.mock.interview.interviewquestion.infra.interview.dto.Message;
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
