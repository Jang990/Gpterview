package com.mock.interview.interviewanswer.application.helper;

import com.mock.interview.interviewanswer.domain.exception.InterviewAnswerNotFoundException;
import com.mock.interview.interviewanswer.infra.AnswerExistsRepository;

public final class AnswerVerifyHelper {
    private AnswerVerifyHelper() {}

    public static void verify(AnswerExistsRepository repository, long answerId, long questionId, long userId) {
        if(!repository.isExist(answerId, questionId, userId))
            throw new InterviewAnswerNotFoundException();
    }
}
