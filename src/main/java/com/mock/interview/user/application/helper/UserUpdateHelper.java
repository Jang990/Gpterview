package com.mock.interview.user.application.helper;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.user.domain.model.Users;

public final class UserUpdateHelper {
    private UserUpdateHelper() {}

    public static void updateCategory(Users users, JobCategoryRepository repository, Long categoryId) {
        if (categoryId == null) {
            users.removeCategory();
            return;
        }

        JobCategory category = repository.findById(categoryId)
                .orElseThrow(JobCategoryNotFoundException::new);
        users.linkCategory(category);
    }

    public static void updatePosition(Users users, JobPositionRepository repository, Long positionId) {
        if (positionId == null) {
            users.removePosition();
            return;
        }

        JobPosition position = repository.findById(positionId)
                .orElseThrow(JobCategoryNotFoundException::new);
        users.linkPosition(position);
    }
}
