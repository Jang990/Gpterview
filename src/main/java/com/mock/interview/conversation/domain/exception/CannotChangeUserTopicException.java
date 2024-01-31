package com.mock.interview.conversation.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class CannotChangeUserTopicException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    private static final String code = "bad.request.change.user.topic";

    public CannotChangeUserTopicException() {
        super(status, code);
    }

    public CannotChangeUserTopicException(Throwable cause) {
        super(cause, status, code);
    }
}
