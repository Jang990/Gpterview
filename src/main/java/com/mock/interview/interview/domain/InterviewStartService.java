package com.mock.interview.interview.domain;

import com.mock.interview.global.RepositoryConst;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.user.domain.exception.DailyInterviewLimitExceededException;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewStartService {
    private final InterviewTimeHolder timeHolder;
    private final ActiveInterviewFinder activeInterviewFinder;
    public void start(Interview interview, Users users) {
        LocalDateTime now = timeHolder.now();
        if(activeInterviewFinder.hasActiveInterview(users, now))
            throw new InterviewAlreadyInProgressException();

        interview.continueInterview(now);
    }
}
