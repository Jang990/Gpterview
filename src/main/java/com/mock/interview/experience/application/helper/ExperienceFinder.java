package com.mock.interview.experience.application.helper;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceRepository;

import java.util.List;

public final class ExperienceFinder {
    private ExperienceFinder() {}

    public static List<Experience> findUserExperiences(ExperienceRepository repository, List<Long> experienceIds, long loginId) {
        List<Experience> experiences = repository.findAllByIdsAndUserID(experienceIds, loginId);
        if(experienceIds.size() != experiences.size())
            throw new ExperienceNotFoundException();
        return experiences;
    }
}
