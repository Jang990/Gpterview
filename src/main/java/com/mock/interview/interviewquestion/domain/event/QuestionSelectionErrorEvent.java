package com.mock.interview.interviewquestion.domain.event;

/** 복구 가능한 에러 */
public record QuestionSelectionErrorEvent(Class<? extends Exception> exceptionClass, long interviewId, long conversationId) {
}
