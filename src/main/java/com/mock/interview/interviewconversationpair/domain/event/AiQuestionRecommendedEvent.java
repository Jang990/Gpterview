package com.mock.interview.interviewconversationpair.domain.event;

import java.util.List;

public record AiQuestionRecommendedEvent(long interviewId, long pairId, List<Long> appearedQuestionIds) {
}
