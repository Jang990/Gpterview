package com.mock.interview.candidate.presentation.web;

import com.mock.interview.candidate.application.CandidateConfigReadOnlyService;
import com.mock.interview.candidate.application.CandidateConfigService;
import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.interview.application.InterviewReadOnlyService;
import com.mock.interview.interview.presentation.dto.InterviewCandidateOverview;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CandidateController {
    private final JobCategoryService categoryService;
    private final CandidateConfigReadOnlyService candidateConfigReadOnlyService;

    @GetMapping(value = {"/interview/candidate/form","/interview/candidate/form/"})
    public String speechPage(
            Model model, @AuthenticationPrincipal Users users
//            @PathVariable(required = false, value = "candidateProfileId") Optional<Long> candidateProfileId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", new InterviewDetailsDto());
        model.addAttribute("candidateConfig", new CandidateConfigForm());
        return "interview/interview-candidate-form";
    }

    @GetMapping("interview/candidate")
    public String loadProfileForInterviewPage(Model model, @AuthenticationPrincipal(expression = "id") Long loginId) {
        model.addAttribute("headerActiveTap", "interview");
        List<InterviewCandidateOverview> overviewList = candidateConfigReadOnlyService.findOverviews(loginId);
        model.addAttribute("candidateOverviewList", overviewList);
        return "/candidate/overview-list";
    }

    @GetMapping("interview/candidate/{candidateId}/form")
    public String loadProfileForInterviewPage(
            Model model,
            @PathVariable(name = "candidateId") long candidateId,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        InterviewSettingDto interviewConfig = candidateConfigReadOnlyService.findCandidate(candidateId, loginId);
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("interviewDetails", interviewConfig.getInterviewDetails());
        model.addAttribute("candidateConfig", interviewConfig.getProfile());
        return "interview/interview-candidate-form";
    }
}
