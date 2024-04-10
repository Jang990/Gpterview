package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;

public interface AiQuestionCreator {
    enum CreationOption {
        NORMAL, CHANGING_TOPIC
    }

    InterviewQuestion createQuestion(long interviewId, CreationOption creationOption);
    RecommendedQuestion create(long interviewId, CreationOption creationOption);
}
