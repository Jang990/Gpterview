package com.mock.interview.interview.domain;

import com.mock.interview.global.RepositoryConst;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.user.domain.exception.DailyInterviewLimitExceededException;
import com.mock.interview.user.domain.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewStartService {
    private final int CURRENT_INTERVIEW_IDX = 0;
    public void start(Interview interview, InterviewRepository repository, Users users) {
        List<Interview> currentInterviewList = repository.findCurrentInterview(users.getId(), RepositoryConst.LIMIT_ONE);
        if(!currentInterviewList.isEmpty())
            verifyCurrentInterview(currentInterviewList.get(CURRENT_INTERVIEW_IDX));

        repository.save(interview);
        interview.continueInterview();
    }

    private void verifyCurrentInterview(Interview currentInterview) {
        if(currentInterview.isActive())
            throw new InterviewAlreadyInProgressException();
        if(currentInterview.isExecutedToday())
            throw new DailyInterviewLimitExceededException();
    }
}
