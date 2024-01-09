package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class IsAlreadyTimeoutInterviewException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.GONE;
    private static final String code = "finished.interview";

    public IsAlreadyTimeoutInterviewException() {
        super(status, code);
    }

    public IsAlreadyTimeoutInterviewException(Throwable cause) {
        super(cause, status, code);
    }
}
