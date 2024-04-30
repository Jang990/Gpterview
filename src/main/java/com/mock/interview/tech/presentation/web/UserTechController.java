package com.mock.interview.tech.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.tech.presentation.dto.TechListForm;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import com.mock.interview.user.application.UserService;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserTechController {
    private final UserRepositoryForView userRepositoryForView;
    private final UserService service;
    @GetMapping("/users/{userId}/tech/form")
    public String userTechForm(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(name = "userId") long userId
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        List<TechViewDto> techList = userRepositoryForView.findUserTech(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("tech", new TechListForm(techList));
        return "/users/tech/tech-form";
    }

    @PostMapping("/users/{userId}/tech")
    public String saveUserTech(
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(name = "userId") long userId,
            TechListForm tech // TODO: tech 입력 오류 처리 필요
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        if(!tech.getTechIds().isEmpty())
            service.replaceTech(userId, tech.getTechIds());
        return "redirect:/users/"+userId;
    }
}
