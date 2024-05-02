package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.ParentQuestionSummaryDto;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class QuestionFormWebController {
    private final QuestionRepositoryForView questionRepositoryForView;
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;

    @GetMapping("question/form")
    public String questionSavePage(Model model) {
        CategoryViewer.setCategoriesView(model, categoryService, positionService);
        model.addAttribute("question", new QuestionForm());
        model.addAttribute("headerActiveTap", "interview-question");
        return "/question/form";
    }

    @GetMapping("/question/{parentQuestionId}/child/form")
    public String selectParentQuestionListPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable("parentQuestionId") long parentQuestionId
    ) {
        ParentQuestionSummaryDto parentQuestionSummary = questionRepositoryForView.findParentQuestionSummary(parentQuestionId);
        if(QuestionPageInitializer.isUnauthorized(loginUserDetail, parentQuestionSummary))
            return "redirect:/question/"+parentQuestionId+"/unauthorized";

        CategoryViewer.setCategoriesView(model, categoryService, positionService);
        model.addAttribute("question", new QuestionForm());
        model.addAttribute("headerActiveTap", "interview-question");

        model.addAttribute("parentQuestion", parentQuestionSummary);
        return "/question/child-form";
    }

    @GetMapping("/question/{questionId}/edit/form")
    public String questionEditPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable("questionId") long questionId
    ) {
        CategoryViewer.setCategoriesView(model, categoryService, positionService);
        QuestionForm question = questionRepositoryForView.findQuestionForm(questionId, loginUserDetail.getId());
        model.addAttribute("question", question);
        model.addAttribute("headerActiveTap", "interview-question");
        return "/question/form";
    }
}
