package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.application.JobCategoryService;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionForm;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InterviewQuestionWebController {
    private final InterviewQuestionRepositoryForView questionRepositoryForView;
    private final JobCategoryService categoryService;

    @GetMapping("question")
    public String questionListPage(Model model, @PageableDefault Pageable pageable) {
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewListWithJobCategory(null, null, pageable);
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionPage", overviewPage);
        return "/question/question-list";
    }

    @GetMapping("question/form")
    public String questionSavePage(Model model, @PageableDefault Pageable pageable) {
        model.addAttribute("categoryList", categoryService.findAllDepartment());
        model.addAttribute("question", new QuestionForm());
        model.addAttribute("headerActiveTap", "interview-question");
        return "/question/question-form";
    }

}
