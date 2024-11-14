package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class RequiredTechTopicNotFoundException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "required.topic.tech";

    public RequiredTechTopicNotFoundException() {
        super(status, code);
    }
}
