package com.mock.interview.user.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CandidateProfileNotFoundException extends RuntimeException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;
    private final String code = "candidate.profile.not.found";
}
