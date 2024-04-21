package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infra.InterviewExistsRepository;
import com.mock.interview.interview.infra.InterviewRepository;

public class InterviewVerificationHelper {
    public static void verify(InterviewExistsRepository interviewRepository, long interviewId, long userId) {
        if (!interviewRepository.isExist(interviewId, userId))
            throw new InterviewNotFoundException();
    }
}
