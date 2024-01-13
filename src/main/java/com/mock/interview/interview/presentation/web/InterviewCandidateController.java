package com.mock.interview.interview.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.interview.application.InterviewReadOnlyService;
import com.mock.interview.interview.presentation.dto.InterviewCandidateOverview;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.candidate.application.CandidateConfigService;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.user.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InterviewCandidateController {

    private final JobCategoryService categoryService;
    private final CandidateConfigService candidateConfigService;
    private final InterviewReadOnlyService interviewReadOnlyService;

    @GetMapping("/interview/setting")
    public String speechPage(
            Model model, @AuthenticationPrincipal Users users
//            @PathVariable(required = false, value = "candidateProfileId") Optional<Long> candidateProfileId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", new InterviewDetailsDto());
        model.addAttribute("candidateConfig", new CandidateConfigForm());
        return "interview/interview-setting";
    }

    @GetMapping("/interview/setting/")
    public String redirectSettingPage() {
        return "redirect:/interview/setting";
    }

    @GetMapping("interview/candidate")
    public String loadProfileForInterviewPage(Model model, @AuthenticationPrincipal(expression = "id") Long loginId) {
        model.addAttribute("headerActiveTap", "interview");
        List<InterviewCandidateOverview> overviewList = interviewReadOnlyService.findOverviews(loginId);
        model.addAttribute("candidateOverviewList", overviewList);
        return "/profile/for-interview";
    }

    @GetMapping("interview/candidate/{interviewId}")
    public String loadProfileForInterviewPage(
            Model model,
            @PathVariable(name = "interviewId") long interviewId,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        InterviewSettingDto interviewConfig = interviewReadOnlyService.findInterviewConfig(interviewId, loginId);
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", interviewConfig.getInterviewDetails());
        model.addAttribute("candidateConfig", interviewConfig.getProfile());
        return "interview/interview-setting";
    }
}
