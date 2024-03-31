package com.mock.interview.interviewquestion.domain.event;

import java.util.List;

public record QuestionRecommendedEvent(long interviewId, long pairId, List<Long> questionIds) {
}
