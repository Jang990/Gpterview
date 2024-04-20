package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceService;
import com.mock.interview.experience.presentation.dto.ExperienceBulkForm;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
public class ExperiencePostController {

    private final ExperienceService experienceService;
    @PostMapping("/users/{username}/experience")
    public String create(
            Model model,
            @AuthenticationPrincipal Users loginUser,
            @PathVariable(value = "username") String username,
            ExperienceBulkForm experienceBulkForm // TODO: 사이즈 관리 필요.
    ) throws AccessDeniedException {
        if (!loginUser.getUsername().equals(username))
            return "redirect:/users/"+username+"/unauthorized";

        experienceService.create(username, experienceBulkForm);
        return "redirect:/users/"+username;
    }
}
