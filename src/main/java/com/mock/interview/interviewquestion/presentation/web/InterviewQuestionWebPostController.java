package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.interviewquestion.application.QuestionSavingService;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.tech.application.TechnicalSubjectsService;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("question/form")
    public String questionSavePage(Model model) {
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("question", new QuestionForm());
        model.addAttribute("headerActiveTap", "interview-question");
        return "/question/question-form";
    }
    @PostMapping("question")
    public String save(@AuthenticationPrincipal(expression = "id") Long loginId, QuestionForm form) {
        List<TechnicalSubjectsResponse> relationalTech = technicalSubjectsService.saveTechIfNotExist(form.getTech());
        long savedId = questionSavingService.save(loginId, form, relationalTech);
        return "redirect:/question";
    }
}
