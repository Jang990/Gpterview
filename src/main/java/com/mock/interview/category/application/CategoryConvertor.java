package com.mock.interview.category.application;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;

public final class CategoryConvertor {
    private CategoryConvertor() {}
    public static JobCategoryView convert(JobCategory category, JobPosition position) {
        return new JobCategoryView(
                category == null ? null : category.getName(),
                position == null ? null : position.getName()
        );
    }

    public static CategoryResponse convert(JobCategory category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    public static CategoryResponse convert(JobPosition position) {
        return new CategoryResponse(position.getId(), position.getName());
    }
}
