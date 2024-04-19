package com.mock.interview.tech.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TechnicalSubjectsNotFoundException extends CustomClientException {
    public TechnicalSubjectsNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.tech");
    }

    public TechnicalSubjectsNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.tech");
    }
}
