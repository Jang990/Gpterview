package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.infra.ai.stage.InterviewProgress;

public record PublishedQuestionInfo(String createdBy, String question, InterviewProgress progress) {
}
