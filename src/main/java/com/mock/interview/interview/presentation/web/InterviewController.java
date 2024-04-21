package com.mock.interview.interview.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.interview.application.InterviewViewService;
import com.mock.interview.interview.infra.InterviewRepositoryForView;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewOverview;
import com.mock.interview.interview.presentation.dto.InterviewProgressDto;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.InfoPageInitializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InterviewController {
    private final InterviewViewService interviewViewService;
    private final UserRepositoryForView userRepositoryForView;
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;
    private final InterviewRepositoryForView interviewRepositoryForView;

    @GetMapping("/interview/{interviewId}")
    public String interviewPage(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        model.addAttribute("headerActiveTap", "interview");

        InterviewProgressDto interviewDto = interviewViewService.findInterview(new InterviewUserIds(interviewId, loginId));
        model.addAttribute("interview", interviewDto);
        List<ConversationContentDto> interviewHistory = interviewViewService.findInterviewHistory(new InterviewUserIds(interviewId, loginId));
        model.addAttribute("messageHistory", interviewHistory);

        if (interviewDto.getExpiredTime().isBefore(LocalDateTime.now())) {
            // TODO: return "면접 진행이 아닌 정보 페이지로 Redirect 필요";
        }

        if (!interviewHistory.isEmpty()) {
            InterviewConversationPairDto lastConversationPair = interviewHistory.get(interviewHistory.size() - 1).getPair();
            model.addAttribute("lastConversationPair", lastConversationPair);
        }
        return "interview/start";
    }

    @GetMapping("/interview/form")
    public String interviewFormPage(
            Model model, @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("interviewDetails", new InterviewConfigForm());

        InterviewAccountForm accountForm = userRepositoryForView.findUserInterviewForm(loginId);
        model.addAttribute("interviewAccount", accountForm);
        CategoryViewer.initInterviewFormPage(model, accountForm.getCategoryId(), categoryService, positionService);
        return "interview/form";
    }

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

    @GetMapping("/users/{username}/interview")
    public String interviewListPage(
            Model model, @AuthenticationPrincipal() Users loginUser,
            @PathVariable("username") String username,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        if (!loginUser.getUsername().equals(username))
            return "redirect:/users/"+username+"/unauthorized";

        Page<InterviewOverview> overviewPage = interviewRepositoryForView.findInterviewList(loginUser.getId(), pageable);
        InterviewPageInitializer.initListPage(model, overviewPage, request);
        return "/interview/list";
    }

}
