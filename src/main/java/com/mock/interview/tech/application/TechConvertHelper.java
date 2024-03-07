package com.mock.interview.tech.application;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

import java.util.List;

public class TechConvertHelper {
    public static List<TechnicalSubjectsResponse> convert(List<TechnicalSubjects> techList) {
        return techList.stream().map(TechConvertHelper::convert).toList();
    }

    public static List<Long> convertToTechId(List<TechnicalSubjectsResponse> techResponseList) {
        return techResponseList.stream().map(TechnicalSubjectsResponse::getId).toList();
    }

    public static TechnicalSubjectsResponse convert(TechnicalSubjects tech) {
        return new TechnicalSubjectsResponse(tech.getId(), tech.getName());
    }
}
