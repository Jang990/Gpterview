package com.mock.interview.interview.presentation.web;

import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.InfoPageInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("isAuthenticated()")
public class InterviewPostResultController {
    @GetMapping("/interview/{interviewId}/expiration/result")
    public String expireInterview(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("interviewId") long interviewId
    ) {
        // TODO: 임시코드
        InfoPageInitializer.initInterviewInfoPage(model, "면접 종료 성공", "진행중인 면접을 성공적으로 종료시켰습니다.", "/");

        return "/info/info";
    }

    @GetMapping("/interview/delete/result")
    public String deleteInterviewResultPage(
            Model model,
            @AuthenticationPrincipal Users loginUser
    ) {
        // TODO: 임시코드
        InfoPageInitializer.initInterviewInfoPage(model, "면접 제거 성공", "면접을 성공적으로 제거했습니다.",
                "/users/"+loginUser.getUsername()+"/interview");

        return "/info/info";
    }
}