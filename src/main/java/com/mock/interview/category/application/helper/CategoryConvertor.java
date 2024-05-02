package com.mock.interview.category.application.helper;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.category.presentation.dto.response.CategoryResponse;
import com.mock.interview.user.domain.model.Users;

public final class CategoryConvertor {
    private CategoryConvertor() {}
    public static JobCategoryView convert(JobCategory category, JobPosition position) {
        return new JobCategoryView(
                category == null ? null : category.getName(),
                position == null ? null : position.getName()
        );
    }

    public static CategoryResponse convert(JobCategory category) {
        return category == null ?
                new CategoryResponse()
                : new CategoryResponse(category.getId(), category.getName());
    }

    public static CategoryResponse convert(JobPosition position) {
        return position == null ?
                new CategoryResponse()
                : new CategoryResponse(position.getId(), position.getName());
    }

    public static JobCategorySelectedIds convertSelectedJobCategoryView(Users users) {
        return new JobCategorySelectedIds(
                users.getCategory() == null ? null : users.getCategory().getId(),
                users.getPosition() == null ? null : users.getPosition().getId()
        );
    }

    public static JobCategorySelectedIds convertSelectedJobCategoryView(JobCategory category, JobPosition position) {
        return new JobCategorySelectedIds(
                category == null ? null : category.getId(),
                position == null ? null : position.getId()
        );
    }
}
