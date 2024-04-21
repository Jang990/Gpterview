package com.mock.interview.tech.application.helper;

import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

import java.util.List;

public class TechConvertHelper {
    public static List<TechnicalSubjectsResponse> convert(List<TechnicalSubjects> techList) {
        return techList.stream().map(TechConvertHelper::convert).toList();
    }

    public static List<TechTopic> convertTopic(List<TechnicalSubjects> techList) {
        return techList.stream().map(TechConvertHelper::convertTopic).toList();
    }

    public static List<Long> convertToTechId(List<TechnicalSubjects> techList) {
        return techList.stream().map(TechnicalSubjects::getId).toList();
    }

    public static TechnicalSubjectsResponse convert(TechnicalSubjects tech) {
        return new TechnicalSubjectsResponse(tech.getId(), tech.getName());
    }

    public static TechTopic convertTopic(TechnicalSubjects tech) {
        return new TechTopic(tech.getId(), tech.getName());
    }
}
