package com.mock.interview.interview.infrastructure.lock.creation;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class AlreadyProceedingInterviewCreation extends CustomClientException {
    public AlreadyProceedingInterviewCreation() {
        super(HttpStatus.CONFLICT, "conflict.interview.creation");
    }

    public AlreadyProceedingInterviewCreation(Throwable cause) {
        super(cause, HttpStatus.CONFLICT , "conflict.interview.creation");
    }
}
