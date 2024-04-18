package com.mock.interview.experience.application;

import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.user.domain.model.Experience;

import java.util.List;

public final class ExperienceConvertor {
    private ExperienceConvertor() {}

    public static List<ExperienceDto> convert(List<Experience> experienceList) {
        return experienceList.stream().map(ExperienceConvertor::convert).toList();
    }

    public static ExperienceDto convert(Experience experience) {
        return new ExperienceDto(experience.getId(), experience.getContent());
    }
}
