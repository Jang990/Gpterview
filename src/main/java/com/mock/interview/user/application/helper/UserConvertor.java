package com.mock.interview.user.application.helper;


import com.mock.interview.category.application.helper.CategoryConvertor;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.dto.AccountUpdateForm;

import java.util.List;

public class UserConvertor {
    public static AccountUpdateForm convertInfo(Users users) {
        return new AccountUpdateForm(
                users.getId(),
                users.getUsername(),
                CategoryConvertor.convertSelectedJobCategoryView(users)
        );
    }

}
