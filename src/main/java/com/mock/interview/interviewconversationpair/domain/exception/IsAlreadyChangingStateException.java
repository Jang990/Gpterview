package com.mock.interview.interviewconversationpair.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class IsAlreadyChangingStateException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    private static final String code = "bad.request.interview.conversation.pair.question.change";

    public IsAlreadyChangingStateException() {
        super(status, code);
    }

    public IsAlreadyChangingStateException(Throwable cause) {
        super(cause, status, code);
    }
}
