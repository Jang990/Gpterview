package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.ActiveInterviewFinder;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.infra.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InterviewStartService {
    private final InterviewTimeHolder timeHolder;
    private final ActiveInterviewFinder activeInterviewFinder;
    private final InterviewRepository repository;
    private final InterviewCreator interviewCreator;

    public Interview start(
            CandidateInfo candidateInfo,
            InterviewTopicDto topics,
            InterviewTimer timer
    ) {
        LocalDateTime now = timeHolder.now();
        if(activeInterviewFinder.hasActiveInterview(candidateInfo.getUsers(), now))
            throw new InterviewAlreadyInProgressException();

        Interview interview = interviewCreator.create(candidateInfo, topics, timer);
        repository.save(interview);

        interview.continueInterview(now);
        return interview;
    }
}
