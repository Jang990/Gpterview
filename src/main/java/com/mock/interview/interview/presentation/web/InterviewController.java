package com.mock.interview.interview.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interview.application.InterviewViewService;
import com.mock.interview.interview.infra.InterviewRepositoryForView;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import com.mock.interview.interview.presentation.dto.*;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
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
        InterviewPageInitializer.init(model);
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
        InterviewPageInitializer.init(model);

        InterviewAccountForm accountForm = userRepositoryForView.findUserInterviewForm(loginId);
        model.addAttribute("configForm", new InterviewConfigForm());
        model.addAttribute("accountForm", accountForm);
        CategoryViewer.showCategoriesView(model, accountForm.getCategoryId(), categoryService, positionService);
        return "interview/form";
    }

    @GetMapping("/users/{userId}/interview")
    public String interviewListPage(
            Model model, @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable("userId") long userId,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        if (!loginUserDetail.getId().equals(userId))
            return "redirect:/users/"+userId+"/unauthorized";

        Page<InterviewOverview> overviewPage = interviewRepositoryForView.findInterviewList(loginUserDetail.getId(), pageable);
        InterviewPageInitializer.initListPage(model, overviewPage, request);
        return "interview/list";
    }

}
