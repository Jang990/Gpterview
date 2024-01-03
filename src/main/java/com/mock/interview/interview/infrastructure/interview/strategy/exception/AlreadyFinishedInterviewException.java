package com.mock.interview.interview.infrastructure.interview.strategy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 이미 종료된 면접 예외.
 */
@Getter
public class AlreadyFinishedInterviewException extends RuntimeException {
    private final HttpStatus status = HttpStatus.PAYLOAD_TOO_LARGE;
    private final String code = "interview.finished";
}
