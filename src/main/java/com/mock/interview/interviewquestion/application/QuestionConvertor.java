package com.mock.interview.interviewquestion.application;

import com.mock.interview.category.application.CategoryConvertor;
import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewPhase;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionTypeForView;
import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

    public static QuestionOverview convert(InterviewQuestion question) {
        return new QuestionOverview(question.getId(), question.getCreatedBy(),
                CategoryConvertor.convert(question.getCategory(), question.getPosition()),
                question.getTechLink().stream().map(link -> link.getTechnicalSubjects().getName()).toList(),
                question.getQuestion(), question.getCreatedAt(), question.getLikes()
        );
    }

    @Nonnull
    public static InterviewQuestionResponse convertInterviewQuestion(@Nullable InterviewQuestion question) {
        return question == null ? new InterviewQuestionResponse() : InterviewQuestionResponse.createQuestion(question.getId(), question.getQuestion());
    }

    @Nonnull
    public static InterviewQuestionResponse convertErrorMessage(@Nonnull String errorMessage) {
        return new InterviewQuestionResponse(null, InterviewRole.SYSTEM, errorMessage);
    }
}
