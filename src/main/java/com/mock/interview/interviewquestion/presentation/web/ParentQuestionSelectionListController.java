package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewquestion.application.QuestionUpdatingService;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ParentQuestionSelectionListController {
    private final QuestionRepositoryForView questionRepositoryForView;
    private final QuestionUpdatingService questionUpdatingService;

    @GetMapping("/question/{childQuestionId}/parent/select")
    public String questionSelectList(
            Model model,
            @PathVariable("childQuestionId") long childQuestionId,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder().searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        model.addAttribute("childQuestionId", childQuestionId);
        return "/question/list-select";
    }

    @PostMapping("/question/{childQuestionId}/parent")
    public String selectParentQuestion(
            @PathVariable("childQuestionId") long childQuestionId,
            @AuthenticationPrincipal(expression = "id") long loginUserId,
            @ModelAttribute("parentQuestionId") Long parentQuestionId
    ) {
        questionUpdatingService.selectParentQuestion(childQuestionId, loginUserId, parentQuestionId);
        return "redirect:/question/" + childQuestionId;
    }
}
