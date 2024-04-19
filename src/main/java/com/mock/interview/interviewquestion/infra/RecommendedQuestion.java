package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.progress.dto.TraceResult;

public record RecommendedQuestion(String createdBy, String question, TraceResult progress) {
}
