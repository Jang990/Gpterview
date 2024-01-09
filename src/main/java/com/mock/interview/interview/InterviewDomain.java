package com.mock.interview.interview;

import com.mock.interview.interview.domain.Interview;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import org.springframework.stereotype.Service;

@Service
public class InterviewDomain {
    public void verifyTime(Interview interview) {
        if (interview.isActive())
            return;

        interview.timeout();
        throw new IsAlreadyTimeoutInterviewException();
    }
}
