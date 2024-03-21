package com.mock.interview.global.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomServerException extends CustomException {
    static final HttpStatus DEFAULT_SERVER_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    static final String DEFAULT_SERVER_ERROR_CODE = "server.error";

    public CustomServerException() {
        super(DEFAULT_SERVER_ERROR_STATUS, DEFAULT_SERVER_ERROR_CODE);
    }

    public CustomServerException(String code) {
        super(DEFAULT_SERVER_ERROR_STATUS, code);
    }

    public CustomServerException(HttpStatus status, String code) {
        super(status, code);
        validateClientError(status);
    }

    public CustomServerException(Throwable cause, String code) {
        super(cause, DEFAULT_SERVER_ERROR_STATUS, code);
    }

    public CustomServerException(Throwable cause, HttpStatus status, String code) {
        super(cause, status, code);
        validateClientError(status);
    }

    private static void validateClientError(HttpStatus status) {
        if(!status.is5xxServerError())
            throw new IllegalArgumentException("5xx status만 가능합니다.");
    }
}
