package com.mock.interview.user.presentation.web;

import com.mock.interview.user.application.UserService;
import com.mock.interview.user.presentation.dto.AccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
    public String indexPage() {
        return "index";
    }

    @PostMapping("auth/sign-up")
    public String signUp(@Valid @ModelAttribute("account") AccountDto accountDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "auth/sign-up";

        userService.create(accountDto);
        return "redirect:/auth/login";
    }
}
