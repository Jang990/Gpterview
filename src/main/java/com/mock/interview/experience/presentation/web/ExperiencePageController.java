package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceViewService;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
import com.mock.interview.global.security.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ExperiencePageController {
    private final ExperienceViewService experienceViewService;

    @GetMapping("/users/{userId}/experience/form")
    public String experienceFormPage(
            Model model,
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(value = "userId") long userId
    ) {
        if (!loginUser.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        model.addAttribute("experienceForm", new ExperienceForm());
        return "/users/experience/experience-form";
    }

    @GetMapping("/users/{userId}/experience/{experienceId}/edit")
    public String experienceEditFormPage(
            Model model,
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(value = "userId") long userId,
            @PathVariable(value = "experienceId") long experienceId
    ) {
        if (!loginUser.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        model.addAttribute("experienceEditForm", experienceViewService.findExperience(experienceId, loginUser.getId()));
        return "/users/experience/experience-edit-form";
    }
}
