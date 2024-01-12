package com.mock.interview.conversation.infrastructure.interview.gpt;

import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class InterviewAIRequest {
    private final List<Message> history;
    private final InterviewSetting interviewSetting;

    public InterviewAIRequest(List<Message> history, InterviewSetting interviewSetting) {
        this.history = history;
        this.interviewSetting = interviewSetting;
    }
}
