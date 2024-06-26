package com.mock.interview.interview.presentation.web;

import com.mock.interview.interview.presentation.dto.InterviewOverview;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class InterviewPageInitializer {
    public static void init(Model model) {
        model.addAttribute("headerActiveTap", "interview");
    }

    public static void initListPage(Model model, Page<InterviewOverview> overviewPage, HttpServletRequest request) {
        init(model);
        model.addAttribute("interviewPage", overviewPage);
        model.addAttribute("currentUri", request.getRequestURI());
    }
}
