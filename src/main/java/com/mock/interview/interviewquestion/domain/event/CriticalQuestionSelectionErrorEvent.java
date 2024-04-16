package com.mock.interview.interviewquestion.domain.event;

/** 복구 불가능한 에러 */
public record CriticalQuestionSelectionErrorEvent(long interviewId, long conversationId) {
}
