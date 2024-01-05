package com.mock.interview.user.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomClientException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "user.not.found");
    }

    public UserNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "user.not.found");
    }
}
