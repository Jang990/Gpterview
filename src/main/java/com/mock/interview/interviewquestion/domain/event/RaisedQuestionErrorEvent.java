package com.mock.interview.interviewquestion.domain.event;

public record RaisedQuestionErrorEvent(long interviewId, String errorMsg) {
}
