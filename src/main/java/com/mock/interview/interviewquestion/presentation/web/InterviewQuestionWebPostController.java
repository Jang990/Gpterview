package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.category.application.JobPositionService;
import com.mock.interview.category.presentation.CategoryValidator;
import com.mock.interview.category.presentation.CategoryViewer;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.interviewquestion.application.QuestionSavingService;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.application.TechnicalSubjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class InterviewQuestionWebPostController {
    private final QuestionSavingService questionSavingService;
    private final TechnicalSubjectsService technicalSubjectsService;
    private final JobCategoryService categoryService;
    private final JobPositionService positionService;

    @GetMapping("question/form")
    public String questionSavePage(Model model) {
        CategoryViewer.setCategoriesView(model, categoryService, positionService);
        model.addAttribute("question", new QuestionForm());
        model.addAttribute("headerActiveTap", "interview-question");
        return "/question/form";
    }
    @PostMapping("question")
    public String save(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            QuestionForm form, BindingResult bindingResult
    ) throws BindException {
        CategoryValidator.validate(bindingResult, new JobCategorySelectedIds(form.getCategoryId(), form.getPositionId()));
        List<Long> relationalTech = technicalSubjectsService.saveTechIfNotExist(form.getTech());
        long savedId = questionSavingService.save(loginId, form, relationalTech);
        return "redirect:/question";
    }
}
