package com.mock.interview.user.domain.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;
    private final String code = "user.not.found";
}
