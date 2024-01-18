package com.mock.interview.user.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import org.springframework.http.HttpStatus;

public class DailyInterviewLimitExceededException extends CustomClientException {
    private static final HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
    private static final String code = "limit.daily.users.interview";

    public DailyInterviewLimitExceededException() {
        super(status, code);
    }

    public DailyInterviewLimitExceededException(Throwable cause) {
        super(cause, status, code);
    }
}
