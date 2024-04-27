package com.mock.interview.interview.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interview.application.InterviewDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InterviewDeleteController {
    private final InterviewDeleteService interviewDeleteService;

    @PostMapping("/interview/{interviewId}/delete")
    public String delete(
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        interviewDeleteService.delete(interviewId, loginUserDetail.getId());
        return "redirect:/interview/delete/result";
    }

}
