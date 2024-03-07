package com.mock.interview.tech.application;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TechnicalSubjectsService {
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    public List<TechnicalSubjectsResponse> saveTechIfNotExist(List<String> skills) {
        List<TechnicalSubjects> techList = technicalSubjectsRepository.findTech(skills);
        List<String> savedNames = techList.stream().map(TechnicalSubjects::getName).toList();
        skills.stream()
                .map(String::toUpperCase)
                .filter(notSavedSkill -> !savedNames.contains(notSavedSkill))
                .map(TechnicalSubjects::create)
                .map(technicalSubjectsRepository::save)
                .forEach(techList::add);

        return techList.stream()
                .map(ts -> new TechnicalSubjectsResponse(ts.getId(), ts.getName()))
                .toList();
    }
}
