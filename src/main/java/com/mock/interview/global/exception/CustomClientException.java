package com.mock.interview.global.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomClientException extends CustomException {
    public CustomClientException(HttpStatus status, String code) {
        super(status, code);
        validateClientError(status);
    }

    public CustomClientException(Throwable cause, HttpStatus status, String code) {
        super(cause, status, code);
        validateClientError(status);
    }

    private static void validateClientError(HttpStatus status) {
        if(!status.is4xxClientError())
            throw new IllegalArgumentException("4xx status만 가능합니다.");
    }
}
