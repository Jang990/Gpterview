package com.mock.interview.interviewconversationpair.domain;

/** 메시지 브로커에게 전달할 용도로 사용 */
public record NewQuestionAddedEvent(long interviewId, long pairId, long questionId) {
}
