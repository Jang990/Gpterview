package com.mock.interview.interview;

import com.mock.interview.interview.domain.Interview;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class InterviewDomain {
    public void verifyTime(Interview interview) {
        if(interview.isActive())
            return;

        interview.timeout();
        throw new IsAlreadyTimeoutInterviewException();
    }

    public void verifyActiveInterview(Interview interview) {
        Objects.requireNonNull(interview);
        if(interview.isActive() && IsActiveTime(interview))
            return;

        if(interview.isActive()) {
            interview.timeout();
        }

        throw new IsAlreadyTimeoutInterviewException();
    }

    private boolean IsActiveTime(Interview interview) {
        return LocalDateTime.now().isBefore(interview.getEndTime());
    }
}
