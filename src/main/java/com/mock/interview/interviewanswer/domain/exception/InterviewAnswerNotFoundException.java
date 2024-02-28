package com.mock.interview.interviewanswer.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InterviewAnswerNotFoundException extends CustomClientException {
    public InterviewAnswerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.interview.answer");
    }

    public InterviewAnswerNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.interview.answer");
    }
}
