package com.mock.interview.interviewconversationpair.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class IsAlreadyCompletedConversationException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "conflict.conversation.completed";

    public IsAlreadyCompletedConversationException() {
        super(status, code);
    }

    public IsAlreadyCompletedConversationException(Throwable cause) {
        super(cause, status, code);
    }
}
