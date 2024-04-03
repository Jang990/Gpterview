package com.mock.interview.user.presentation.web;

import com.mock.interview.category.presentation.CategoryValidator;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.user.application.UserService;
import com.mock.interview.user.presentation.dto.AccountForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserCreationController {
    private final UserService userService;

    @PostMapping("auth/sign-up")
    public String signUp(
            @Valid @ModelAttribute("account") AccountForm form,
            BindingResult bindingResult
    ) throws BindException {
        CategoryValidator.validate(bindingResult, new JobCategorySelectedIds(form.getCategoryId(), form.getPositionId()));

        userService.create(form);
        return "redirect:/auth/login";
    }
}
