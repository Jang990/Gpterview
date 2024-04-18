package com.mock.interview.tech.application;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;

import java.util.List;
import java.util.Optional;

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

    public static TechnicalSubjects saveTechIfNotExist(TechnicalSubjectsRepository technicalSubjectsRepository, String techName) {
        TechnicalSubjects technicalSubject = technicalSubjectsRepository.findTech(techName).orElseGet(() -> TechnicalSubjects.create(techName));
        technicalSubjectsRepository.save(technicalSubject);
        return technicalSubject;
    }
}
