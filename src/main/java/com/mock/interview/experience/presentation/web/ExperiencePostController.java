package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceDeleteService;
import com.mock.interview.experience.application.ExperienceService;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
import com.mock.interview.global.security.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ExperiencePostController {

    private final ExperienceService experienceService;
    private final ExperienceDeleteService experienceDeleteService;
    @PostMapping("/users/{userId}/experience")
    public String create(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(value = "userId") long userId,
            ExperienceForm experienceForm
    ) {
        if (!loginUser.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        experienceService.create(loginUser.getId(), experienceForm);
        return "redirect:/users/"+userId;
    }

    @PostMapping("/users/{userId}/experience/{experienceId}/delete")
    public String delete(
            Model model,
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(value = "userId") long userId,
            @PathVariable(value = "experienceId") long experienceId
    ) {
        if (!loginUser.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        experienceDeleteService.delete(experienceId, loginUser.getId());
        return "redirect:/users/"+userId;
    }
}
