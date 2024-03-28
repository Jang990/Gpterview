package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infra.InterviewRepository;

public class InterviewVerificationHelper {
    public static void verify(InterviewRepository interviewRepository, long interviewId, long userId) {
        if (interviewRepository.existsByIdAndUsersId(interviewId, userId))
            return;

        throw new InterviewNotFoundException();
    }
}
