package com.mock.interview.interviewquestion.infra.interview.gpt;

import com.mock.interview.interviewquestion.infra.interview.setting.InterviewSetting;
import com.mock.interview.interviewquestion.infra.interview.dto.Message;
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
