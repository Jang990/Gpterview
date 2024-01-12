package com.mock.interview.interview.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.interview.presentation.dto.CandidateProfileForm;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.candidate.application.CandidateProfileService;
import com.mock.interview.user.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InterviewCandidateController {

    private final JobCategoryService categoryService;
    private final CandidateProfileService candidateProfileService;

    @GetMapping("/interview/candidate/profile/{profileId}")
    public String loadProfileInInterviewSettingPage(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "profileId") long profileId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", new InterviewDetailsDto());
        CandidateProfileForm form = candidateProfileService.findProfile(profileId, loginId);
        System.out.println(form);
        model.addAttribute("candidateProfile", form);
        return "interview/interview-setting";
    }

    @GetMapping("/interview/setting")
    public String speechPage(
            Model model, @AuthenticationPrincipal Users users
//            @PathVariable(required = false, value = "candidateProfileId") Optional<Long> candidateProfileId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", new InterviewDetailsDto());
        model.addAttribute("candidateProfile", new CandidateProfileForm());
        return "interview/interview-setting";
    }

    @GetMapping("/interview/setting/")
    public String redirectSettingPage() {
        return "redirect:/interview/setting";
    }
}
