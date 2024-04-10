package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;

public interface AiQuestionCreator {
    InterviewQuestion create(long interviewId);
    InterviewQuestion changeTopic(long interviewId);
}
