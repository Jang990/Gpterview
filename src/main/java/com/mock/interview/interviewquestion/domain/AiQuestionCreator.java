package com.mock.interview.interviewquestion.domain;

import com.mock.interview.interviewquestion.infra.RecommendedQuestion;

public interface AiQuestionCreator {
    enum CreationOption {
        NORMAL, CHANGING_TOPIC
    }

    RecommendedQuestion create(long interviewId, CreationOption creationOption);
}
