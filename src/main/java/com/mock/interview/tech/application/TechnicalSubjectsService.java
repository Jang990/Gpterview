package com.mock.interview.tech.application;

import com.mock.interview.tech.application.helper.TechConvertHelper;
import com.mock.interview.tech.application.helper.TechSavingHelper;
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

    public long create(String techName) {
        TechnicalSubjects tech = technicalSubjectsRepository.findTech(techName)
                .orElseGet(() -> TechnicalSubjects.create(techName));
        technicalSubjectsRepository.save(tech);
        return tech.getId();
    }

    public List<Long> saveTechIfNotExist(List<String> skills) {
        List<TechnicalSubjects> techList = TechSavingHelper.saveTechIfNotExist(technicalSubjectsRepository, skills);
        return TechConvertHelper.convertToTechId(techList);
    }
}
