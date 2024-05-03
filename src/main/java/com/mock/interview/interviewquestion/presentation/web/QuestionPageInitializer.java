package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForm;
import com.mock.interview.interviewquestion.presentation.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

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

    public static void initQuestionDetail(
            Model model, QuestionDetailDto questionDetail,
            List<AnswerDetailDto> answerTop3,
            List<ChildQuestionOverview> childQuestionTop3
    ) {
        questionDetail.setAnswerTop3(answerTop3);
        questionDetail.setChildQuestionTop3(childQuestionTop3);
        model.addAttribute("questionDetailPage", questionDetail);
        model.addAttribute("answer", new AnswerForm());
    }

    public static void initQuestionOverview(Model model, QuestionOverview questionOverview) {
        model.addAttribute("question", questionOverview);
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
