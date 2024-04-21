package com.mock.interview.category.application.helper;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;

public class JobConnectionHelper {
    public static void connect(
            JobCategoryRepository categoryRepository, JobPositionRepository positionRepository,
            InterviewQuestion question, Long categoryId, Long positionId
    ) {
        if (categoryId == null)
            throw new IllegalArgumentException("categoryId는 필수");

        if (positionId == null) {
            JobCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(JobCategoryNotFoundException::new);
            question.linkCategory(category);
            return;
        }

        JobPosition position = positionRepository.findById(positionId)
                .orElseThrow(JobCategoryNotFoundException::new);
        JobCategory category = position.getCategory();
        question.linkJob(category, position);
    }
}
