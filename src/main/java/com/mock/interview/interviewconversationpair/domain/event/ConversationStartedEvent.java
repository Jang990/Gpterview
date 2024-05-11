package com.mock.interview.interviewconversationpair.domain.event;

import java.util.List;

public record ConversationStartedEvent(long interviewId, long pairId, List<Long> appearedQuestionIds) {
}
