package com.mock.interview.interviewquestion.domain.event;

/** 복구 불가능한 에러 */
public record CriticalQuestionSelectionErrorEvent(Class<? extends Throwable> exceptionClass, long interviewId, long conversationId) {
}
