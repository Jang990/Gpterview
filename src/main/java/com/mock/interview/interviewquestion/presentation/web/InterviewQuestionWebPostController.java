package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.presentation.CategoryValidator;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.interviewquestion.application.QuestionDeleteService;
import com.mock.interview.interviewquestion.application.QuestionSavingService;
import com.mock.interview.interviewquestion.application.QuestionUpdatingService;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class InterviewQuestionWebPostController {
    private final QuestionSavingService questionSavingService;
    private final QuestionDeleteService questionDeleteService;
    private final QuestionUpdatingService questionUpdatingService;

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
            @PathVariable("questionId") long questionId,
            QuestionForm form, BindingResult bindingResult
    ) throws BindException {
        CategoryValidator.validate(bindingResult, new JobCategorySelectedIds(form.getCategoryId(), form.getPositionId()));
        questionUpdatingService.update(loginId, questionId, form);
        return "redirect:/question/"+questionId;
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

    @PostMapping("question/{questionId}/parent/delete")
    public String deleteParentQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable("questionId") long questionId
    ) {
        questionDeleteService.deleteParent(questionId, loginId);
        return "redirect:/question/" + questionId;
    }
}
