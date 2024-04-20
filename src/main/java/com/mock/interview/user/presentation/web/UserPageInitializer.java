package com.mock.interview.user.presentation.web;

import com.mock.interview.user.infrastructure.UserRepositoryForView;
import org.springframework.ui.Model;

public final class UserPageInitializer {
    private UserPageInitializer() {}

    public static void initUserDetailPage(Model model, UserRepositoryForView repositoryForView, String username) {
        initUsername(model, username);
        model.addAttribute("account", repositoryForView.findUserDetail(username));
    }

    public static void initAnotherUserPage(Model model, String username) {
        initUsername(model, username);
    }

    public static void initUsername(Model model, String username) {
        model.addAttribute("username", username);
    }

}
