package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InterviewQuestionWebController {
    private final InterviewQuestionRepositoryForView questionRepositoryForView;

    @GetMapping("question")
    public String questionListPage(Model model, @PageableDefault Pageable pageable) {
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewListWithJobCategory(null, null, pageable);
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionPage", overviewPage);
        return "/question/question-list";
    }

}
