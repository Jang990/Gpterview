package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceDeleteService;
import com.mock.interview.experience.application.ExperienceService;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
import com.mock.interview.user.domain.model.Users;
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
    @PostMapping("/users/{username}/experience")
    public String create(
            @AuthenticationPrincipal Users loginUser,
            @PathVariable(value = "username") String username,
            ExperienceForm experienceForm
    ) {
        if (!loginUser.getUsername().equals(username))
            return "redirect:/users/"+username+"/unauthorized";

        experienceService.create(loginUser.getId(), experienceForm);
        return "redirect:/users/"+username;
    }

    @PostMapping("/users/{username}/experience/{experienceId}/delete")
    public String delete(
            Model model,
            @AuthenticationPrincipal Users loginUser,
            @PathVariable(value = "username") String username,
            @PathVariable(value = "experienceId") long experienceId
    ) {
        if (!loginUser.getUsername().equals(username))
            return "redirect:/users/"+username+"/unauthorized";

        experienceDeleteService.delete(experienceId,loginUser.getId());
        return "redirect:/users/"+username;
    }
}
