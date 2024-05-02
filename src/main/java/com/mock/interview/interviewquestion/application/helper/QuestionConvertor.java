package com.mock.interview.interviewquestion.application.helper;

import com.mock.interview.category.application.helper.CategoryConvertor;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import com.mock.interview.interviewquestion.domain.model.QuestionType;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionTypeForView;
import com.mock.interview.interviewquestion.presentation.dto.response.InterviewQuestionResponse;
import com.mock.interview.tech.application.helper.TechConvertHelper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

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

    public static QuestionTypeForView convert(QuestionType type) {
        return switch (type) {
            case TECHNICAL -> QuestionTypeForView.TECHNICAL;
            case PERSONALITY -> QuestionTypeForView.PERSONALITY;
            case EXPERIENCE -> QuestionTypeForView.EXPERIENCE;
        };
    }

    public static QuestionOverview convert(InterviewQuestion question) {
        return new QuestionOverview(question.getId(), question.getCreatedBy(),
                CategoryConvertor.convertView(question.getCategory()),
                CategoryConvertor.convertView(question.getPosition()),
                TechConvertHelper.convertView(question.getTechLink().stream().map(QuestionTechLink::getTechnicalSubjects).toList()),
                question.getQuestion(), question.getCreatedAt(),
                getParentId(question.getParentQuestion()), question.getOwner().getId(),
                question.getLikes(), question.isHidden()
        );
    }

    public static QuestionForm convertForm(InterviewQuestion question) {
        return new QuestionForm(
                CategoryConvertor.convertSelectedJobCategoryView(question.getCategory(), question.getPosition()),
                TechConvertHelper.convertView(question.getTechLink().stream().map(QuestionTechLink::getTechnicalSubjects).toList()),
                convert(question.getQuestionType()),
                question.getQuestion(),
                question.getId()
        );
    }

    private static Long getParentId(InterviewQuestion parentQuestion) {
        return parentQuestion == null ? null : parentQuestion.getId();
    }

    public static List<QuestionOverview> convert(List<InterviewQuestion> questionList) {
        return questionList.stream().map(QuestionConvertor::convert).toList();
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
