package com.mock.interview.interview;

import com.mock.interview.interview.domain.exception.InterviewNotExpiredException;
import com.mock.interview.interview.domain.model.Interview;
import org.springframework.stereotype.Service;

@Service
public class InterviewDomain {

    public boolean tryTimeOut(Interview interview) {
        try {
            interview.timeout();
        } catch (InterviewNotExpiredException e) {
            return false;
        }

        return true;
    }
}
