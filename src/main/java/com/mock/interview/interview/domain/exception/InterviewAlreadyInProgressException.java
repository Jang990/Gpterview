package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class InterviewAlreadyInProgressException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "conflict.interview.progress";

    public InterviewAlreadyInProgressException() {
        super(status, code);
    }

    public InterviewAlreadyInProgressException(Throwable cause) {
        super(cause, status, code);
    }
}
