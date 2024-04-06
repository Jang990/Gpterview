package com.mock.interview.interview.infra;

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

@Service
@RequiredArgsConstructor
public class InterviewStarterImpl implements InterviewStarter {
    @Override
    public Interview start(Users users, InterviewConfigForm interviewConfig) {
        Interview interview = Interview.startInterview(interviewConfig, users, users.getCategory(), users.getPosition());
        switch (interviewConfig.getInterviewType()) {
            case TECHNICAL -> connectUsersTech(interview, users);
            case EXPERIENCE -> connectUsersExperience(interview, users, interviewConfig.getSelectedExperience());
            case PERSONALITY -> { /* 딱히 할게 없다. */ }
            default -> throw new IllegalArgumentException("지원하지 않는 면접 타입");
        }

        return interview;
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
