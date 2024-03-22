package com.mock.interview.tech.application;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;

import java.util.List;

public class TechSavingHelper {
    public static List<TechnicalSubjects> saveTechIfNotExist(TechnicalSubjectsRepository technicalSubjectsRepository, List<String> skills) {
        List<TechnicalSubjects> techList = technicalSubjectsRepository.findTech(skills);
        List<String> savedNames = techList.stream().map(TechnicalSubjects::getName).toList();
        skills.stream()
                .map(String::toUpperCase)
                .filter(notSavedSkill -> !savedNames.contains(notSavedSkill))
                .map(TechnicalSubjects::create)
                .map(technicalSubjectsRepository::save)
                .forEach(techList::add);
        return techList;
    }
}
