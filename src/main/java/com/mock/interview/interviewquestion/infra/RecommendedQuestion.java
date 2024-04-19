package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.progress.dto.InterviewProgress;

public record RecommendedQuestion(String createdBy, String question, InterviewProgress progress) {
}
