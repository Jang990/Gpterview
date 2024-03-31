package com.mock.interview.interviewconversationpair.domain.event;

/** 메시지 브로커에게 전달할 용도로 사용 */
public record QuestionConnectedEvent(long interviewId, long pairId, long questionId) {
}
