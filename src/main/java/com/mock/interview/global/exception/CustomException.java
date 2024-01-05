package com.mock.interview.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public CustomException(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    public CustomException(Throwable cause, HttpStatus status, String code) {
        super(cause);
        this.status = status;
        this.code = code;
    }
}
