package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewquestion.presentation.dto.ParentQuestionSummaryDto;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class QuestionPageInitializer {
    public static void initListPage(Model model, Page<QuestionOverview> overviewPage, QuestionSearchCond searchCond, HttpServletRequest request) {
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionPage", overviewPage);
        model.addAttribute("currentUri", request.getRequestURI());
        initQuestionSearchForm(model, searchCond);
    }

    public static void initQuestionSearchForm(Model model, QuestionSearchCond searchCond) {
        model.addAttribute("questionSearchCond", searchCond);
    }

    public static void initEmptyQuestionSearchForm(Model model) {
        model.addAttribute("questionSearchCond", new QuestionSearchCond());
    }

    public static boolean isUnauthorized(LoginUserDetail users, QuestionOverview question) {
        if(!question.isHidden())
            return false;

        return users == null || !users.getUsername().equals(question.getCreatedBy());
    }

    public static boolean isUnauthorized(LoginUserDetail users, ParentQuestionSummaryDto question) {
        if(!question.isHidden())
            return false;

        return users == null || !users.getId().equals(question.getOwnerId());
    }
}
