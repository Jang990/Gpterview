package com.mock.interview.user.application;

import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.user.application.helper.UserUpdateHelper;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.user.presentation.dto.AccountUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JobCategoryRepository categoryRepository;
    private final JobPositionRepository positionRepository;

    public void update(AccountUpdateForm form) {
        Users users = repository.findById(form.getUserId())
                .orElseThrow(UserNotFoundException::new);

        UserUpdateHelper.updateCategory(users, categoryRepository, form.getCategoryId());
        UserUpdateHelper.updatePosition(users, positionRepository, form.getPositionId());
        users.changeUsername(form.getUsername());
    }
}
