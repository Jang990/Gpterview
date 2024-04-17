package com.mock.interview.interview.infra;

import com.mock.interview.global.RepositoryConst;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.user.domain.exception.DailyInterviewLimitExceededException;
import com.mock.interview.user.domain.model.Experience;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.domain.InterviewStarter;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.InterviewTechLink;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewStarterImpl implements InterviewStarter {
    private final InterviewRepository repository;
    @Override
    public Interview start(Users users, InterviewConfigForm interviewConfig) {
        Optional<Interview> currentInterviewOpt = repository.findCurrentInterview(users.getId(), RepositoryConst.LIMIT_ONE);
        if (currentInterviewOpt.isPresent()) {
            verifyCurrentInterview(currentInterviewOpt.get());
        }

        Interview interview = Interview.startInterview(interviewConfig, users, users.getCategory(), users.getPosition());
        switch (interviewConfig.getInterviewType()) {
            case TECHNICAL -> connectUsersTech(interview, users);
            case EXPERIENCE -> connectUsersExperience(interview, users, interviewConfig.getSelectedExperience());
            case PERSONALITY -> { /* 딱히 할게 없다. */ }
            default -> throw new IllegalArgumentException("지원하지 않는 면접 타입");
        }

        return interview;
    }

    private void verifyCurrentInterview(Interview currentInterview) {
        if(currentInterview.isActive())
            throw new InterviewAlreadyInProgressException();
        if(currentInterview.isExecutedToday())
            throw new DailyInterviewLimitExceededException();
    }


    private void connectUsersTech(Interview interview, Users users) {
        List<TechnicalSubjects> userTech = users.getTechLink().stream().map(UsersTechLink::getTechnicalSubjects).toList();
        if(!userTech.isEmpty())
            linkUniqueTech(interview, userTech);
    }


    private void connectUsersExperience(Interview interview, Users users, List<Long> selectedExperience) {
        List<Experience> experiences = users.getExperiences()
                .stream().filter(experience -> selectedExperience.contains(experience.getId())).toList();
        interview.linkExperience(experiences);
    }

    public void linkUniqueTech(Interview interview, List<TechnicalSubjects> techs) {
        if (interview.getTechLink().isEmpty()) {
            interview.linkTech(techs);
            return;
        }

        List<Long> linkedTechIds = getLinkedTechIds(interview);
        for (TechnicalSubjects tech : techs) {
            if(linkedTechIds.contains(tech.getId()))
                continue;
            interview.linkTech(tech);
        }
    }

    private List<Long> getLinkedTechIds(Interview interview) {
        return interview.getTechLink()
                .stream().map(InterviewTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getId).toList();
    }

}
