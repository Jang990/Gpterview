package com.mock.interview.user.domain;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersTechLinker {
    public void lineUniqueTech(Users users, List<TechnicalSubjects> techs) {
        if (users.getTechLink().isEmpty()) {
            users.linkTech(techs);
            return;
        }

        List<Long> linkedTechIds = getLinkedTechIds(users);
        for (TechnicalSubjects tech : techs) {
            if(linkedTechIds.contains(tech.getId()))
                continue;
            users.linkTech(tech);
        }
    }

    private static List<Long> getLinkedTechIds(Users users) {
        return users.getTechLink()
                .stream().map(UsersTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getId).toList();
    }
}
