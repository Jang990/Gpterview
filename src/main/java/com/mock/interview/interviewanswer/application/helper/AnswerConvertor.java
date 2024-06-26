package com.mock.interview.interviewanswer.application.helper;

import com.mock.interview.interviewanswer.presentation.dto.InterviewAnswerRequest;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class AnswerConvertor {
    private AnswerConvertor() {}

    @Nonnull
    public static InterviewAnswerRequest convert(@Nullable InterviewAnswer answer) {
        return answer == null ? new InterviewAnswerRequest() : new InterviewAnswerRequest(answer.getId(), answer.getAnswer());
    }
}
