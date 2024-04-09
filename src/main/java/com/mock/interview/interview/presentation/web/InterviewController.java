package com.mock.interview.interview.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import com.mock.interview.interviewconversationpair.presentation.dto.InterviewConversationPairDto;
import com.mock.interview.interviewconversationpair.presentation.dto.PairStatusForView;
import com.mock.interview.interviewquestion.application.QuestionRecommendationService;
import com.mock.interview.user.infrastructure.UserRepositoryForView;
import com.mock.interview.user.presentation.InfoPageInitializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InterviewController {

    private final InterviewConversationRepositoryForView conversationRepositoryForView;
    private final QuestionRecommendationService questionRecommendationService;
    private final UserRepositoryForView userRepositoryForView;
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;

    @GetMapping("/interview/{interviewId}")
    public String interviewPage(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("interviewId", interviewId);

        Slice<ConversationContentDto> interviewConversations = conversationRepositoryForView.findInterviewConversations(interviewId, loginId, PageRequest.of(0, 25));
        model.addAttribute("messageHistory", interviewConversations);
        if (!interviewConversations.hasContent()) {
            return "interview/start";
        }

        List<ConversationContentDto> content = interviewConversations.getContent();
        InterviewConversationPairDto lastConversationPair = content.get(content.size() - 1).getPair();
        model.addAttribute("lastConversationPair", lastConversationPair);
        if (lastConversationPair.getStatus() == PairStatusForView.RECOMMENDING) {
            model.addAttribute("recommendationQuestions",
                    questionRecommendationService.findRecommendation(interviewId, lastConversationPair.getId()));
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
        InfoPageInitializer.initInterviewInfoPage(model, "면접 만료 성공", "진행중인 면접을 성공적으로 만료시켰습니다.", "/");

        return "/info/info";
    }

}
