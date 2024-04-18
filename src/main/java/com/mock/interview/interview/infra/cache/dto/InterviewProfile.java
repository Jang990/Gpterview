package com.mock.interview.interview.infra.cache.dto;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.experience.presentation.dto.api.ExperienceResponse;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;

import java.util.List;

public record InterviewProfile(CategoryResponse category, CategoryResponse field, List<TechnicalSubjectsResponse> skills, List<ExperienceResponse> experience) {

}
