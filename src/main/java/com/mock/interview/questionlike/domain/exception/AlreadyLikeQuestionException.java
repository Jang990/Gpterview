package com.mock.interview.questionlike.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class AlreadyLikeQuestionException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "conflict.question.like";

    public AlreadyLikeQuestionException() {
        super(status, code);
    }

    public AlreadyLikeQuestionException(Throwable cause) {
        super(cause, status, code);
    }
}
