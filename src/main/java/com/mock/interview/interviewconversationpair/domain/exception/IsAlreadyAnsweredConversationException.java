package com.mock.interview.interviewconversationpair.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class IsAlreadyAnsweredConversationException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.GONE;
    private static final String code = "finished.conversation";

    public IsAlreadyAnsweredConversationException() {
        super(status, code);
    }

    public IsAlreadyAnsweredConversationException(Throwable cause) {
        super(cause, status, code);
    }
}
