package com.mock.interview.interviewquestion.application;

import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewPhase;
import com.mock.interview.interviewquestion.presentation.dto.QuestionTypeForView;

public class QuestionConvertor {
    public static QuestionType convert(InterviewPhase phase) {
        return switch (phase) {
            case TECHNICAL -> QuestionType.TECHNICAL;
            case EXPERIENCE -> QuestionType.EXPERIENCE;
            case PERSONAL -> QuestionType.PERSONALITY;
        };
    }

    public static QuestionType convert(QuestionTypeForView type) {
        return switch (type) {
            case TECHNICAL -> QuestionType.TECHNICAL;
            case EXPERIENCE -> QuestionType.EXPERIENCE;
            case PERSONALITY -> QuestionType.PERSONALITY;
        };
    }
}
