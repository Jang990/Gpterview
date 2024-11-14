package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class RequiredExperienceTopicNotFoundException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.CONFLICT;
    private static final String code = "required.topic.experience";

    public RequiredExperienceTopicNotFoundException() {
        super(status, code);
    }
}
