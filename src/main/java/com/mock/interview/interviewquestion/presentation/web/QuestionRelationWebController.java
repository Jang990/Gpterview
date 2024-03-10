package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewquestion.infra.InterviewQuestionRepositoryForView;
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
public class QuestionRelationWebController {
    private final InterviewQuestionRepositoryForView questionRepositoryForView;

    @GetMapping("question/{questionId}/child")
    public String childQuestionListPage(Model model, @PageableDefault Pageable pageable) {
        // 임시 코드
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewListWithJobCategory(null, null, pageable);
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionPage", overviewPage);
        return "/question/question-list";
    }

    @GetMapping("question/form/parent")
    public String selectChildQuestionPage(Model model, @PageableDefault Pageable pageable) {
        // 임시 코드
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewListWithJobCategory(null, null, pageable);
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionPage", overviewPage);
        return "/question/question-list";
    }
}
