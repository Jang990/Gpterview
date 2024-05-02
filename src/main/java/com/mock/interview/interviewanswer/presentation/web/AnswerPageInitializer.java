package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.interviewanswer.presentation.dto.AnswerForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public final class AnswerPageInitializer {
    private AnswerPageInitializer() {}

    public static void initListPage(Model model, long questionId, Page<AnswerForView> overviewPage, HttpServletRequest request) {
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionId", questionId);
        model.addAttribute("answerPage", overviewPage);
        model.addAttribute("currentUri", request.getRequestURI());
    }
}
