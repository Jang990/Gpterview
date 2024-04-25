package com.mock.interview.interview.domain;

import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.user.domain.model.Users;

public interface InterviewStarter {
    Interview start(Users users, InterviewConfigForm interviewConfig, InterviewAccountForm accountForm);
}
