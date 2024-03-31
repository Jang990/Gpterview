package com.mock.interview.interview.domain;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.user.domain.model.Users;
import org.springframework.stereotype.Service;

@Service
public class InterviewCreator {
    public Interview startInterview(
            InterviewRepository interviewRepository,
            CandidateConfig config, Users user, JobPosition position
    ) {
        if (interviewRepository.findActiveInterview(user.getId()).isPresent()) // TODO: QueryDSL로 최적화
            throw new InterviewAlreadyInProgressException();

        Interview interview = Interview.startInterview(config, user, position);
        interview = interviewRepository.save(interview);
        return interview;
    }
}
