package com.mock.interview.interview.domain;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.user.domain.model.Users;
import org.springframework.stereotype.Service;

@Service
public class InterviewCreator {
    public Interview startInterview(
            InterviewRepository interviewRepository,
            CandidateConfig config, Users user, JobCategory appliedJob
    ) {
        if (interviewRepository.findActiveInterview(user.getId()).isPresent()) // TODO: QueryDSL로 최적화
            throw new InterviewAlreadyInProgressException();

        Interview interview = Interview.startInterview(config, user, appliedJob);
        interview = interviewRepository.save(interview);

        Events.raise(new InterviewContinuedEvent(interview.getId()));
        return interview;
    }
}
