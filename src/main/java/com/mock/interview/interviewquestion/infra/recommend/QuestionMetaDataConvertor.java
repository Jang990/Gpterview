package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.mock.interview.tech.application.helper.TechConvertHelper;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class QuestionMetaDataConvertor {
    private QuestionMetaDataConvertor() {}

    public static List<QuestionMetaData> convert(List<InterviewQuestion> question) {
        return question.stream().map(QuestionMetaDataConvertor::convert).collect(Collectors.toList());
    }

    public static QuestionMetaData convert(InterviewQuestion question) {
        return new QuestionMetaData(
                question.getId(),
                (question.getParentQuestion() == null) ? null : question.getParentQuestion().getId(),
                (question.getPosition() == null) ? null : question.getPosition().getName(),
                convertTech(question.getTechLink().stream().map(QuestionTechLink::getTechnicalSubjects).toList()),
                question.getQuestionToken().getResult(),
                question.getLikes()
        );
    }

    private static List<String> convertTech(List<TechnicalSubjects> technicalSubjectsList) {
        return TechConvertHelper.convert(technicalSubjectsList).stream().map(TechnicalSubjectsResponse::getName).toList();
    }
}
