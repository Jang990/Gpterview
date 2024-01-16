package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class InterviewNotExpiredException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    private static final String code = "not.expired.interview";

    public InterviewNotExpiredException() {
        super(status, code);
    }

    public InterviewNotExpiredException(Throwable cause) {
        super(cause, status, code);
    }
}
