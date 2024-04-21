package com.mock.interview.interviewquestion.application;

import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.infra.QuestionExistsRepository;

public final class QuestionVerifyHelper {
    private QuestionVerifyHelper() {}

    public static void verify(QuestionExistsRepository repository, long questionId, long userId) {
        if(!repository.isExist(questionId, userId))
            throw new InterviewQuestionNotFoundException();
    }
}
