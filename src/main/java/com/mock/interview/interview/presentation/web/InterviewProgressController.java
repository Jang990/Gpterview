package com.mock.interview.interview.presentation.web;

import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.user.application.rate.WithUserInterviewRateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InterviewProgressController {

    private final InterviewService interviewService;

    @PostMapping("/interview")
    @WithUserInterviewRateLimiter
    public String startInterviewRequest(
            InterviewConfigForm configForm,
            InterviewAccountForm accountForm,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        long interviewId = interviewService.createCustomInterview(loginId, configForm, accountForm);
        return "redirect:/interview/" + interviewId;
    }

    @PostMapping("/interview/{interviewId}/expiration")
    public String expireInterview(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("interviewId") long interviewId
    ) {
        InterviewUserIds lockDto = new InterviewUserIds(interviewId, loginId);
        interviewService.expireInterview(lockDto);
        return "redirect:/interview/" + interviewId + "/expiration/result";
    }
}
