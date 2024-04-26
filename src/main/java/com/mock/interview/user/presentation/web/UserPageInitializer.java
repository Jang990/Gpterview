package com.mock.interview.user.presentation.web;

import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.dto.AccountDetailDto;
import org.springframework.ui.Model;

public final class UserPageInitializer {
    private UserPageInitializer() {}

    public static void initUserDetailPage(Model model, UserRepositoryForView repositoryForView, long userId) {
        AccountDetailDto userDetail = repositoryForView.findUserDetail(userId);
        model.addAttribute("account", userDetail);
    }

}
