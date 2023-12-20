package com.mock.interview.infrastructure.interview.strategy.exception;

/**
 * 이미 종료된 면접 예외.
 */
public class AlreadyFinishedInterviewException extends RuntimeException {
    private final String code = "interview.finished";
}
