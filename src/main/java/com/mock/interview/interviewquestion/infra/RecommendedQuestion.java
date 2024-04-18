package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;

import java.util.List;

public record RecommendedQuestion(String createdBy, String question, InterviewProgress progress, String topic) {
}
