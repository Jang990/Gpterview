package com.mock.interview.candidate.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CandidateConfigNotFoundException extends CustomClientException {
    public CandidateConfigNotFoundException() {
        super(HttpStatus.NOT_FOUND, "candidate.profile.not.found");
    }

    public CandidateConfigNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "candidate.profile.not.found");
    }
}
