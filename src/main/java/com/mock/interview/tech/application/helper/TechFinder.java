package com.mock.interview.tech.application.helper;

import com.mock.interview.tech.domain.exception.TechnicalSubjectsNotFoundException;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;

import java.util.List;

public final class TechFinder {
    private TechFinder() {}

    public static List<TechnicalSubjects> findTechs(TechnicalSubjectsRepository technicalSubjectsRepository, List<Long> ids) {
        List<TechnicalSubjects> techs = technicalSubjectsRepository.findAllById(ids);
        if(techs.size() != ids.size())
            throw new TechnicalSubjectsNotFoundException();
        return techs;
    }
}
