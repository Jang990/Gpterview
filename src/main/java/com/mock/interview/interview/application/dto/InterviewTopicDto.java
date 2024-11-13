package com.mock.interview.interview.application.dto;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Builder
@Getter
public class InterviewTopicDto {
    private JobCategory category;
    private JobPosition position;
}
