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
import com.mock.interview.user.presentation.dto.AccountDto;
import com.mock.interview.user.presentation.dto.AccountForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;

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
}
