package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;

public final class InterviewFindingHelper {
    private InterviewFindingHelper() {}

    public static Interview find(InterviewRepository repository, InterviewUserIds interviewDto) {
        return repository.findByIdAndUserId(interviewDto.interviewId(), interviewDto.userId())
                .orElseThrow(InterviewNotFoundException::new);
    }
}
