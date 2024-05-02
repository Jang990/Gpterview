package com.mock.interview.tech.application.helper;

import com.mock.interview.interviewquestion.infra.QuestionTechLinkRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.domain.model.QuestionTechLink;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import com.mock.interview.user.infrastructure.UsersTechLinkRepository;

import java.util.List;

public final class TechConnectHelper {
    private TechConnectHelper() {}

    public static void replaceUserTech(
            TechnicalSubjectsRepository technicalSubjectsRepository,
            UsersTechLinkRepository usersTechLinkRepository,
            Users users, List<Long> techRequest
    ) {
        List<Long> existsTechIds = users.getTechLink().stream()
                .map(UsersTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getId).toList();

        List<Long> newTechIds = techRequest.stream()
                .filter(newId -> !existsTechIds.contains(newId)).toList();
        if (!newTechIds.isEmpty()) {
            List<TechnicalSubjects> newTechList = technicalSubjectsRepository
                    .findAllById(newTechIds);
            users.linkTech(newTechList);
        }

        List<Long> techIdsToDelete = existsTechIds.stream()
                .filter((existsId) -> !techRequest.contains(existsId)).toList();
        if(!techIdsToDelete.isEmpty())
            usersTechLinkRepository.deleteUserTech(users.getId(), techIdsToDelete);
    }

    public static void replaceQuestionTech(
            TechnicalSubjectsRepository technicalSubjectsRepository,
            QuestionTechLinkRepository questionTechLinkRepository,
            InterviewQuestion question, List<Long> techRequest
    ) {
        List<Long> existsTechIds = question.getTechLink().stream()
                .map(QuestionTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getId).toList();

        List<Long> newTechIds = techRequest.stream()
                .filter(newId -> !existsTechIds.contains(newId)).toList();
        if (!newTechIds.isEmpty()) {
            List<TechnicalSubjects> newTechList = technicalSubjectsRepository
                    .findAllById(newTechIds);
            question.linkTech(newTechList);
        }

        List<Long> techIdsToDelete = existsTechIds.stream()
                .filter((existsId) -> !techRequest.contains(existsId)).toList();
        if(!techIdsToDelete.isEmpty())
            questionTechLinkRepository.deleteQuestionTech(question.getId(), techIdsToDelete);
    }
}
