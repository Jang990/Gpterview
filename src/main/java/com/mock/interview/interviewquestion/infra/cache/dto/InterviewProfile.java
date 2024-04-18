package com.mock.interview.interviewquestion.infra.cache.dto;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

import java.util.List;

public record InterviewProfile(CategoryResponse category, CategoryResponse field, List<TechnicalSubjectsResponse> skills, List<ExperienceDto> experience) {

}
