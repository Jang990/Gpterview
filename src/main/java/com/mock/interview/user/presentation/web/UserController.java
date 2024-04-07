package com.mock.interview.user.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.infra.InterviewRepositoryForView;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.review.presentation.dto.ReviewIndexPageFragment;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.dto.AccountDto;
import com.mock.interview.user.presentation.dto.AccountForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;
    private final UserRepositoryForView repositoryForView;

    @GetMapping("auth/login")
    public String loginPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/auth/login";
    }

    @GetMapping("auth/sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("account", new AccountForm());
        CategoryViewer.setCategoriesView(model, categoryService, positionService);
        return "/auth/sign-up";
    }

    @GetMapping("/auth/{username}")
    public String myPage(Model model, @PathVariable(value = "username") String username) {
        // TODO: 세션정보와 일치하는지 확인할 것
        System.out.println(username);
        model.addAttribute("account", repositoryForView.findUserDetail(username));
        return "/auth/my-page";
    }
}
