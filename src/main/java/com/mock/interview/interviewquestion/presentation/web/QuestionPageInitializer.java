package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class QuestionPageInitializer {
    public static void initListPage(Model model, Page<QuestionOverview> overviewPage, QuestionSearchCond searchCond) {
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionSearchCond", searchCond);
        model.addAttribute("questionPage", overviewPage);
    }
}
