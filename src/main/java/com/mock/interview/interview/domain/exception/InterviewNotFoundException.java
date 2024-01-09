package com.mock.interview.interview.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InterviewNotFoundException extends CustomClientException {
    public InterviewNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.interview");
    }

    public InterviewNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.interview");
    }
}
