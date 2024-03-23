package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class NotEnoughRelatedQuestionException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    private static final String code = "not.enough.related.question";

    public NotEnoughRelatedQuestionException() {
        super(status, code);
    }

    public NotEnoughRelatedQuestionException(Throwable cause) {
        super(cause, status, code);
    }
}
