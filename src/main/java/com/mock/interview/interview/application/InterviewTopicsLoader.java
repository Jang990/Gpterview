package com.mock.interview.interview.application;

import com.mock.interview.experience.application.helper.ExperienceFinder;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interview.domain.InterviewTopicsDto;
import com.mock.interview.interview.domain.InterviewTopicsDtoCreator;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.application.helper.TechFinder;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewTopicsLoader {
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final ExperienceRepository experienceRepository;
    private final InterviewTopicsDtoCreator topicsDtoCreator;

    protected InterviewTopicsDto load(
            long userId,
            InterviewType type,
            InterviewAccountForm accountForm) {
        return topicsDtoCreator.create(
                type,
                loadTechs(accountForm.getTechIds()),
                loadUserExperiences(userId, accountForm.getExperienceIds())
        );
    }

    private List<TechnicalSubjects> loadTechs(List<Long> techIds) {
        return TechFinder.findTechs(technicalSubjectsRepository, techIds);
    }

    private List<Experience> loadUserExperiences(long loginId, List<Long> experienceIds) {
        return ExperienceFinder.findUserExperiences(experienceRepository, experienceIds, loginId);
    }
}
