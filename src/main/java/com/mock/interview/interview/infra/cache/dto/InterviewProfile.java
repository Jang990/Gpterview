package com.mock.interview.interview.infra.cache.dto;

import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;

import java.util.List;

public record InterviewProfile(CategoryResponse category, CategoryResponse field, List<TechTopic> skills, List<ExperienceTopic> experience) {

}
