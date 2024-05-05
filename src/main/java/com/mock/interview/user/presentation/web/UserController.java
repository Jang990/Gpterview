package com.mock.interview.user.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.dto.AccountDto;
import com.mock.interview.user.presentation.dto.UnauthorizedPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepositoryForView repositoryForView;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "users/login";
    }

    @GetMapping("/users/{userId}")
    public String myPage(Model model, @PathVariable(value = "userId") long userId) {
        // TODO: 세션정보와 일치하는지 확인할 것
        UserPageInitializer.initUserDetailPage(model, repositoryForView, userId);
        return "users/my-page";
    }

    @GetMapping("/users/{userId}/unauthorized")
    public String unauthorizedPage(Model model, @PathVariable(value = "username") long userId) {
        model.addAttribute("info", new UnauthorizedPageInfo("접근 권한 없음", String.valueOf(userId), "/"));
        return "users/unauthorized";
    }
}
