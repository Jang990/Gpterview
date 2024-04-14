package com.mock.interview.interview.presentation.web;

import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewLockDto;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewStartingDto;
import com.mock.interview.user.presentation.InfoPageInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InterviewPostController {

    private final InterviewService interviewService;

    @PostMapping("/interview")
    public String startInterviewRequest(
            InterviewConfigForm interviewConfigForm,
            @AuthenticationPrincipal(expression = "id") Long loginId, Model model
    ) {
        InterviewStartingDto interviewStartingDto = interviewService.createCustomInterview(loginId, interviewConfigForm);
        model.addAttribute("interviewId", interviewStartingDto.getInterviewId());
        model.addAttribute("lastConversationPair", interviewStartingDto.getPair());
        return "redirect:/interview/" + interviewStartingDto.getInterviewId();
    }

    @PostMapping("/interview/{interviewId}/expiration")
    public String expireInterview(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("interviewId") long interviewId
    ) {
        InterviewLockDto lockDto = new InterviewLockDto(loginId, interviewId);
        interviewService.expireInterview(lockDto);
        return "redirect:/interview/" + interviewId + "/expiration/result";
    }
}
