package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.InterviewTopicsDto;
import com.mock.interview.interview.domain.model.*;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.infra.lock.creation.InterviewCreationUserLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewTopicsLoader topicsLoader;
    private final CandidateLoader candidateLoader;

    private final InterviewStartService interviewStartService;
    private final InterviewTimerCreator timerCreator;

    @InterviewCreationUserLock
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long createCustomInterview(
            long loginId,
            InterviewConfigForm interviewConfig,
            InterviewAccountForm accountForm) {
        CandidateInfo candidateInfo = candidateLoader.load(
                loginId,
                accountForm.getCategoryId(),
                accountForm.getPositionId()
        );
        InterviewTopicsDto topics = topicsLoader.load(
                loginId,
                interviewConfig.getInterviewType(),
                accountForm
        );
        InterviewTimer timer = timerCreator.create(
                interviewConfig.getDurationMinutes()
        );

        return interviewStartService
                .start(candidateInfo, topics, timer)
                .getId();
    }
}
