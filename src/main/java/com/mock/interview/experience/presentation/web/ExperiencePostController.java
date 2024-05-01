package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceDeleteService;
import com.mock.interview.experience.application.ExperienceService;
import com.mock.interview.experience.presentation.dto.ExperienceDeleteOptions;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
import com.mock.interview.global.security.dto.LoginUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId,
            ExperienceForm experienceForm
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        experienceService.create(loginUserDetail.getId(), experienceForm);
        return "redirect:/users/"+userId+"/experience";
    }

    @PostMapping("/users/{userId}/experience/{experienceId}/delete")
    public String delete(
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId,
            @PathVariable(value = "experienceId") long experienceId,
            ExperienceDeleteOptions options
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        if(options.isDeleteRelatedQuestion())
            experienceDeleteService.deleteWithQuestion(experienceId, loginUserDetail.getId());
        else
            experienceDeleteService.delete(experienceId, loginUserDetail.getId());
        return "redirect:/users/"+userId+"/experience";
    }

    @PostMapping("/users/{userId}/experience/{experienceId}/edit")
    public String update(
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId,
            @PathVariable(value = "experienceId") long experienceId,
            ExperienceForm editForm
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        experienceService.update(experienceId, loginUserDetail.getId(), editForm);
        return "redirect:/users/"+userId+"/experience";
    }
}
