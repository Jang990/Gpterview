package com.mock.interview.user.application;

import com.mock.interview.interview.presentation.dto.CandidateProfileDto;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.presentation.dto.CandidateProfileOverviewDto;

import java.util.List;

public class UserConvertor {
    public static List<CandidateProfileOverviewDto> entityToOverviewDto(List<CandidateProfile> entityList) {
        return entityList.stream()
                .map((entity) -> new CandidateProfileOverviewDto(
                        entity.getId(),
                        entity.getTitle(), entity.getDepartment(),
                        entity.getField(), entity.getCreatedAt()
                )).toList();
    }

    public static CandidateProfileDto entityToDto(CandidateProfile profile) {
        return new CandidateProfileDto(
                profile.getDepartment(), profile.getField(),
                profile.getSkills(), profile.getExperience()
        );
    }
}
