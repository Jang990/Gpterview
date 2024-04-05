package com.mock.interview.interview.domain;

import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.user.domain.model.Users;

public interface InterviewStarter {
    Interview start(Users users, InterviewConfigDto interviewConfig);
}
