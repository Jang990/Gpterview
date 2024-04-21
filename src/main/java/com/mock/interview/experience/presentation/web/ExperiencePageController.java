package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceViewService;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.web.UserPageInitializer;
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

    @GetMapping("/users/{username}/experience/form")
    public String experienceFormPage(
            Model model,
            @AuthenticationPrincipal Users loginUser,
            @PathVariable(value = "username") String username
    ) {
        if (!loginUser.getUsername().equals(username))
            return "redirect:/users/"+username+"/unauthorized";

        UserPageInitializer.initUsername(model, username);
        model.addAttribute("experienceForm", new ExperienceForm());
        return "/users/experience/experience-form";
    }

    @GetMapping("/users/{username}/experience/{experienceId}/edit")
    public String experienceEditFormPage(
            Model model,
            @AuthenticationPrincipal Users loginUser,
            @PathVariable(value = "username") String username,
            @PathVariable(value = "experienceId") long experienceId
    ) {
        if (!loginUser.getUsername().equals(username))
            return "redirect:/users/"+username+"/unauthorized";

        UserPageInitializer.initUsername(model, username);
        model.addAttribute("experienceEditForm", experienceViewService.findExperience(experienceId, loginUser.getId()));
        return "/users/experience/experience-edit-form";
    }
}
