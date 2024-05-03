package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public final class AnswerPageInitializer {
    private AnswerPageInitializer() {}

    public static void initListPage(Model model, long questionId, Page<AnswerDetailDto> overviewPage, HttpServletRequest request) {
        initAnswerPageHeaderSelected(model);
        model.addAttribute("questionId", questionId);
        model.addAttribute("answerPage", overviewPage);
        model.addAttribute("currentUri", request.getRequestURI());
    }

    public static void initAnswerPageHeaderSelected(Model model) {
        model.addAttribute("headerActiveTap", "interview-question");
    }
}
