package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryValidator;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewquestion.application.QuestionDeleteService;
import com.mock.interview.interviewquestion.application.QuestionSavingService;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.interviewquestion.presentation.dto.ParentQuestionSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class InterviewQuestionWebPostController {
    private final QuestionRepositoryForView questionRepositoryForView;
    private final QuestionSavingService questionSavingService;
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;
    private final QuestionDeleteService questionDeleteService;

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

    @PostMapping("question")
    public String save(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            QuestionForm form, BindingResult bindingResult
    ) throws BindException {
        CategoryValidator.validate(bindingResult, new JobCategorySelectedIds(form.getCategoryId(), form.getPositionId()));
        return "redirect:/question/" + questionSavingService.save(loginId, form);
    }

    @PostMapping("question/{questionId}/delete")
    public String delete(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("questionId") long questionId
    ) {
        questionDeleteService.delete(questionId, loginId);
        return "redirect:/question";
    }

    @PostMapping("question/{questionId}/edit")
    public String edit(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("questionId") long questionId
    ) {
        // TODO: 수정 로직 작성
        return "redirect:/question";
    }

    @PostMapping("question/{parentQuestionId}/child")
    public String saveChildQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("parentQuestionId") long parentQuestionId,
            QuestionForm form, BindingResult bindingResult
    ) throws BindException {
        CategoryValidator.validate(bindingResult, new JobCategorySelectedIds(form.getCategoryId(), form.getPositionId()));
        return "redirect:/question/" + questionSavingService.save(loginId, parentQuestionId, form);
    }
}
