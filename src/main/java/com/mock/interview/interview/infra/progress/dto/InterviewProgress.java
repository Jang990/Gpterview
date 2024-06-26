package com.mock.interview.interview.infra.progress.dto;

import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;

public record InterviewProgress(InterviewPhase phase, InterviewTopic<?> interviewTopic, double progress) {
    public InterviewProgress {
        verify(phase, interviewTopic);
    }

    private void verify(InterviewPhase phase, InterviewTopic<?> interviewTopic) {
        if(phase == InterviewPhase.EXPERIENCE && !(interviewTopic instanceof ExperienceTopic))
            throw new IllegalArgumentException("InterviewProgress.EXPERIENCE 예외");
        if(phase == InterviewPhase.TECHNICAL && !(interviewTopic instanceof TechTopic))
            throw new IllegalArgumentException("InterviewProgress.TECHNICAL 예외");
        if(phase == InterviewPhase.PERSONAL && interviewTopic != null)
            throw new IllegalArgumentException("InterviewProgress.PERSONAL 예외");
    }

    public Long getTopicId() {
        return interviewTopic.getId();
    }

    public String getTopicContent() {
        if(interviewTopic == null)
            return null;
        return interviewTopic.getContent();
    }
}
