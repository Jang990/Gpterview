package com.mock.interview.experience.application;

import com.mock.interview.experience.presentation.dto.api.ExperienceResponse;
import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.user.domain.model.Experience;

import java.util.List;

public final class ExperienceConvertor {
    private ExperienceConvertor() {}

    public static List<ExperienceResponse> convert(List<Experience> experienceList) {
        return experienceList.stream().map(ExperienceConvertor::convert).toList();
    }

    public static List<ExperienceTopic> convertTopic(List<Experience> experienceList) {
        return experienceList.stream().map(ExperienceConvertor::convertTopic).toList();
    }

    public static ExperienceResponse convert(Experience experience) {
        return new ExperienceResponse(experience.getId(), experience.getContent());
    }

    public static ExperienceTopic convertTopic(Experience experience) {
        return new ExperienceTopic(experience.getId(), experience.getContent());
    }
}
