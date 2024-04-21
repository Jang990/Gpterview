package com.mock.interview.user.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.dto.AccountDto;
import com.mock.interview.user.presentation.dto.AccountForm;
import com.mock.interview.user.presentation.dto.UnauthorizedPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;
    private final UserRepositoryForView repositoryForView;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/users/login";
    }

    @GetMapping("/sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/users/sign-up";
    }

    @GetMapping("/users/{username}")
    public String myPage(Model model, @PathVariable(value = "username") String username) {
        // TODO: 세션정보와 일치하는지 확인할 것
        UserPageInitializer.initUserDetailPage(model, repositoryForView, username);
        return "/users/my-page";
    }

    @GetMapping("/users/{username}/unauthorized")
    public String unauthorizedPage(Model model, @PathVariable(value = "username") String username) {
        model.addAttribute("info", new UnauthorizedPageInfo("접근 권한 없음", username, "/"));
        return "/users/unauthorized";
    }
}
