package com.mock.interview.interviewconversationpair.domain.exception;

import com.mock.interview.global.exception.CustomClientException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InterviewConversationPairNotFoundException extends CustomClientException {
    public InterviewConversationPairNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not.found.interview.conversation.pair");
    }

    public InterviewConversationPairNotFoundException(Throwable cause) {
        super(cause, HttpStatus.NOT_FOUND, "not.found.interview.conversation.pair");
    }
}
