package com.mock.interview.interviewquestion.infra.recommend.exception;

public class NotEnoughQuestion extends Exception {
    private static final String errorMsg = "추천받고 싶은 질문 수보다 전체 질문 수가 적음";

    public NotEnoughQuestion() {
        super(errorMsg);
    }
}
