package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;

public record PublishedQuestion(String createdBy, String question, InterviewProgress progress) {
}
