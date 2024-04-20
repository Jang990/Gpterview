package com.mock.interview.experience.application;

import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceExistsRepository;

public final class ExperienceVerifyHelper {
    private ExperienceVerifyHelper() {}

    public static void verify(ExperienceExistsRepository repository, long experienceId, long userId) {
        Integer result = repository.exist(experienceId, userId);
        if(isEmpty(result))
            throw new ExperienceNotFoundException();
    }

    private static boolean isEmpty(Integer isExists) {
        return isExists == null;
    }
}
