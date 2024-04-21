package com.mock.interview.experience.application.helper;

import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceExistsRepository;

public final class ExperienceVerifyHelper {
    private ExperienceVerifyHelper() {}

    public static void verify(ExperienceExistsRepository repository, long experienceId, long userId) {
        if(!repository.isExist(experienceId, userId))
            throw new ExperienceNotFoundException();
    }
}
