package com.mock.interview.interview.domain;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.domain.exception.RequiredExperienceTopicNotFoundException;
import com.mock.interview.interview.domain.exception.RequiredTechTopicNotFoundException;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewTopicsDtoCreator {
    public InterviewTopicsDto create(
            InterviewType type,
            List<TechnicalSubjects> techs,
            List<Experience> experiences) {
        // TODO: 좀 더 깐깐하게 검사해야 하나? -> type에서 요구하는 토픽이 없는데 들어온 경우 등등...
        if(type.requiredTechTopics() && techs.isEmpty())
            throw new RequiredTechTopicNotFoundException();
        if (type.requiredExperienceTopics() && experiences.isEmpty())
            throw new RequiredExperienceTopicNotFoundException();

        return createDto(type, techs, experiences);
    }

    private static InterviewTopicsDto createDto(InterviewType type, List<TechnicalSubjects> techs, List<Experience> experiences) {
        if(type.requiredTechTopics() && type.requiredExperienceTopics())
            return InterviewTopicsDto.ofTypeWithAll(type, experiences, techs);
        if(type.requiredExperienceTopics())
            return InterviewTopicsDto.ofTypeWithExperiences(type, experiences);
        if(type.requiredTechTopics())
            return InterviewTopicsDto.ofTypeWithTechs(type, techs);
        return InterviewTopicsDto.ofType(type);
    }
}
