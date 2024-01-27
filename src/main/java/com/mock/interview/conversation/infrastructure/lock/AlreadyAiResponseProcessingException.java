package com.mock.interview.conversation.infrastructure.lock;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class AlreadyAiResponseProcessingException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "conflict.interview.ai.response";
    public AlreadyAiResponseProcessingException() {
        super(status, code);
    }

    public AlreadyAiResponseProcessingException(Throwable cause) {
        super(cause, status , code);
    }
}
