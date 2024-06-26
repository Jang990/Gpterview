package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.experience.infra.ExperienceRepositoryView;
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
    private final ExperienceRepositoryView experienceRepositoryView;

    @GetMapping("question/form")
    public String questionSavePage(Model model, @AuthenticationPrincipal LoginUserDetail loginUserDetail) {
        CategoryViewer.showCategoriesView(model, null, categoryService, positionService);
        QuestionPageInitializer.initQuestionForm(model, experienceRepositoryView, loginUserDetail);
        model.addAttribute("headerActiveTap", "interview-question");
        return "question/form";
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

        CategoryViewer.showCategoriesView(model, null, categoryService, positionService);
        QuestionPageInitializer.initQuestionForm(model, experienceRepositoryView, loginUserDetail);
        model.addAttribute("headerActiveTap", "interview-question");

        model.addAttribute("parentQuestion", parentQuestionSummary);
        return "question/child-form";
    }

    @GetMapping("/question/{questionId}/edit/form")
    public String questionEditPage(
            Model model,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @PathVariable("questionId") long questionId
    ) {
        CategoryViewer.showCategoriesView(model, null, categoryService, positionService);
        QuestionForm questionForm = questionRepositoryForView.findQuestionForm(questionId, loginUserDetail.getId());
        QuestionPageInitializer.initEditQuestionForm(model, questionForm, experienceRepositoryView, loginUserDetail);
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("existsQuestionId", questionId);
        return "question/form";
    }
}
