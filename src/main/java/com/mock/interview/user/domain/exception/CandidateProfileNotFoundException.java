package com.mock.interview.user.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CandidateProfileNotFoundException extends CustomClientException {
    public CandidateProfileNotFoundException() {
        super(HttpStatus.NOT_FOUND, "candidate.profile.not.found");
    }

    public CandidateProfileNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "candidate.profile.not.found");
    }
}
