package com.mock.interview.interviewquestion.domain;

import java.util.List;

public record QuestionRecommendedEvent(long interviewId, long pairId, List<Long> questionIds) {
}
