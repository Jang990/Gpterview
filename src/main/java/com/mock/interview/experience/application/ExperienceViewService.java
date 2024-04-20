package com.mock.interview.experience.application;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.experience.presentation.dto.ExperienceEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperienceViewService {
    private final ExperienceRepository repository;

    public ExperienceEditForm findExperience(long experienceId, long userId) {
        Experience experience = repository.findByIdAndUserId(experienceId, userId)
                .orElseThrow(ExperienceNotFoundException::new);
        return ExperienceConvertor.convertEditForm(experience);
    }

}
