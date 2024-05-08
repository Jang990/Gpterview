package com.mock.interview.user.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.user.application.UserService;
import com.mock.interview.user.application.session.UserSessionService;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.dto.AccountUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserFormController {
    private final UserService service;
    private final UserSessionService sessionService;
    private final UserRepositoryForView repositoryForView;
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;

    @GetMapping("/users/{userId}/form")
    public String userFormPage(
            Model model, @PathVariable(value = "userId") long userId,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        AccountUpdateForm form = repositoryForView.findUserUpdateForm(userId);
        CategoryViewer.showCategoriesView(model, form.getCategoryId(), categoryService, positionService);
        model.addAttribute("account", form);
        return "users/form";
    }

    @PostMapping("/users/{userId}")
    public String userFormPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId,
            AccountUpdateForm form
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        service.update(form);
//        sessionService.updateSession(form.getUsername());
        return "redirect:/users/"+userId;
    }
}
