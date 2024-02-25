package com.mock.interview.interviewquestion.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InterviewQuestionNotFoundException extends CustomClientException {
    public InterviewQuestionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.interview.question");
    }

    public InterviewQuestionNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.interview.question");
    }
}