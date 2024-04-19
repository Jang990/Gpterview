package com.mock.interview.experience.domain;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExperienceNotFoundException extends CustomClientException {
    public ExperienceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.experience");
    }

    public ExperienceNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.experience");
    }
}
