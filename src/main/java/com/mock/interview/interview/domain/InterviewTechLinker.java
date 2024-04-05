package com.mock.interview.interview.domain;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.InterviewTechLink;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewTechLinker {
    public void linkUniqueTech(Interview interview, List<TechnicalSubjects> techs) {
        System.out.println(techs);
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

    private static List<Long> getLinkedTechIds(Interview interview) {
        return interview.getTechLink()
                .stream().map(InterviewTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getId).toList();
    }
}
