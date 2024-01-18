package com.mock.interview.user.presentation.web;

import com.mock.interview.global.exception.CustomClientException;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.user.application.UserService;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.dto.AccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final InterviewService interviewService;

    @GetMapping("auth/login")
    public String loginPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/auth/login";
    }

    @GetMapping("auth/sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/auth/sign-up";
    }

    @GetMapping("/")
    public String indexPage(Model model, @AuthenticationPrincipal Users users) {
        if(users != null)
            model.addAttribute("activeInterview", getActiveInterview(users.getId()));
        else
            model.addAttribute("activeInterview", new InterviewResponse());

        return "index";
    }

    private InterviewResponse getActiveInterview(long loginId) {
        return interviewService.findActiveInterview(loginId)
                .orElseGet(InterviewResponse::new);
    }

    @GetMapping("/auth/{username}")
    public String myPage(@PathVariable(value = "username") String username) {
        // TODO: 세션정보와 일치하는지 확인할 것
        return "/auth/my-page";
    }

    @PostMapping("auth/sign-up")
    public String signUp(@Valid @ModelAttribute("account") AccountDto accountDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "auth/sign-up";

        userService.create(accountDto);
        return "redirect:/auth/login";
    }
}
