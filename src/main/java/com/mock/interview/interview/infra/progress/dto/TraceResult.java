package com.mock.interview.interview.infra.progress.dto;

import com.mock.interview.experience.presentation.dto.api.ExperienceResponse;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

public record TraceResult(InterviewPhase phase, InterviewTopic interviewTopic, double progress) {
    public TraceResult {
        verify(phase, interviewTopic);
    }

    private void verify(InterviewPhase phase, InterviewTopic interviewTopic) {
        if(phase == InterviewPhase.EXPERIENCE && !(interviewTopic instanceof ExperienceResponse))
            throw new IllegalArgumentException("TraceResult.EXPERIENCE 예외");
        if(phase == InterviewPhase.TECHNICAL && !(interviewTopic instanceof TechnicalSubjectsResponse))
            throw new IllegalArgumentException("TraceResult.TECHNICAL 예외");
        if(phase == InterviewPhase.PERSONAL && interviewTopic != null)
            throw new IllegalArgumentException("TraceResult.PERSONAL 예외");
    }

    public Long getTopicId() {
        return interviewTopic.getId();
    }

    public String getTopicContent() {
        return interviewTopic.getName();
    }
}
