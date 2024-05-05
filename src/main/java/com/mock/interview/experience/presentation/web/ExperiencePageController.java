package com.mock.interview.experience.presentation.web;

import com.mock.interview.experience.application.ExperienceViewService;
import com.mock.interview.experience.infra.ExperienceRepositoryView;
import com.mock.interview.experience.presentation.dto.ExperienceDeleteOptions;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
import com.mock.interview.global.security.dto.LoginUserDetail;
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
    private final ExperienceRepositoryView repositoryView;

    @GetMapping("/users/{userId}/experience")
    public String experienceListPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        model.addAttribute("userId", userId);
        model.addAttribute("experienceList", repositoryView.findExperienceList(userId));
        model.addAttribute("deleteOptions", new ExperienceDeleteOptions());
        return "users/experience/experience-list";
    }

    @GetMapping("/users/{userId}/experience/form")
    public String experienceFormPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        model.addAttribute("experienceForm", new ExperienceForm());
        return "users/experience/experience-form";
    }

    @GetMapping("/users/{userId}/experience/{experienceId}/edit")
    public String experienceEditFormPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(value = "userId") long userId,
            @PathVariable(value = "experienceId") long experienceId
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        model.addAttribute("experienceId", experienceId);
        model.addAttribute("experienceEditForm", experienceViewService.findExperience(experienceId, loginUserDetail.getId()));
        return "users/experience/experience-edit-form";
    }
}
