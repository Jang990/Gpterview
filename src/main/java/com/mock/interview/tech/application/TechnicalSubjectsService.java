package com.mock.interview.tech.application;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TechnicalSubjectsService {
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    public List<Long> saveTechIfNotExist(List<String> skills) {
        List<TechnicalSubjects> techList = TechSavingHelper.saveTechIfNotExist(technicalSubjectsRepository, skills);
        return TechConvertHelper.convertToTechId(techList);
    }
}
