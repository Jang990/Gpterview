package com.mock.interview.user.application;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.candidate.presentation.dto.CandidateConfigOverviewDto;

import java.util.List;

public class UserConvertor {
    public static List<CandidateConfigOverviewDto> entityToOverviewDto(List<CandidateConfig> entityList) {
        return entityList.stream()
                .map((entity) -> new CandidateConfigOverviewDto(
                        entity.getId(),
                        entity.getTitle(), entity.getDepartment().getName(),
                        entity.getField().getName(), entity.getCreatedAt()
                )).toList();
    }

    public static CandidateConfigForm entityToDto(CandidateConfig profile) {
        return new CandidateConfigForm(
                profile.getField().getId(),
                profile.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                profile.getExperience()
        );
    }
}
