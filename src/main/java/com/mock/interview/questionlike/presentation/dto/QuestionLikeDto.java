package com.mock.interview.questionlike.presentation.dto;

import com.mock.interview.questionlike.infra.lock.QuestionLikeLockable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionLikeDto implements QuestionLikeLockable {
    private long userId;
    private long questionId;
}
