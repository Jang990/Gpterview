package com.mock.interview.questionlike.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class QuestionLikeNotFoundException extends CustomClientException {
    public QuestionLikeNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.interview.question.like");
    }

    public QuestionLikeNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.interview.question.like");
    }
}