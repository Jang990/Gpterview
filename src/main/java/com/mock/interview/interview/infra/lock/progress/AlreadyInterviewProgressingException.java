package com.mock.interview.interview.infra.lock.progress;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class AlreadyInterviewProgressingException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "conflict.interview.progress.response";
    public AlreadyInterviewProgressingException() {
        super(status, code);
    }

    public AlreadyInterviewProgressingException(Throwable cause) {
        super(cause, status , code);
    }
}
