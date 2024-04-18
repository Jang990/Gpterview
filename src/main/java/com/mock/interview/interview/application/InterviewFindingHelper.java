package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewLockDto;

public final class InterviewFindingHelper {
    private InterviewFindingHelper() {}

    public static Interview find(InterviewRepository repository, InterviewLockDto interviewDto) {
        return repository.findByIdAndUserId(interviewDto.interviewId(), interviewDto.userId())
                .orElseThrow(InterviewNotFoundException::new);
    }
}
