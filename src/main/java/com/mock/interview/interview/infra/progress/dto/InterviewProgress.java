package com.mock.interview.interview.infra.progress.dto;

import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;

public record InterviewProgress(InterviewPhase phase, InterviewTopic interviewTopic, double progress) {
    public InterviewProgress {
        verify(phase, interviewTopic);
    }

    private void verify(InterviewPhase phase, InterviewTopic interviewTopic) {
        if(phase == InterviewPhase.EXPERIENCE && !(interviewTopic instanceof ExperienceTopic))
            throw new IllegalArgumentException("TraceResult.EXPERIENCE 예외");
        if(phase == InterviewPhase.TECHNICAL && !(interviewTopic instanceof TechTopic))
            throw new IllegalArgumentException("TraceResult.TECHNICAL 예외");
        if(phase == InterviewPhase.PERSONAL && interviewTopic != null)
            throw new IllegalArgumentException("TraceResult.PERSONAL 예외");
    }

    public Long getTopicId() {
        return interviewTopic.getId();
    }

    public String getTopicContent() {
        return interviewTopic.getContent();
    }
}
