package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.experience.infra.ExperienceRepositoryView;
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
            List<ChildQuestionOverview> childQuestionTop3,
            LoginUserDetail loginUserDetail
    ) {
        if(questionDetail.getExperience() != null
                && isInaccessibleExperience(questionDetail, loginUserDetail))
            questionDetail.removeExperience();
        questionDetail.setAnswerTop3(answerTop3);
        questionDetail.setChildQuestionTop3(childQuestionTop3);
        model.addAttribute("questionDetailPage", questionDetail);
        model.addAttribute("answer", new AnswerForm());
    }

    private static boolean isInaccessibleExperience(QuestionDetailDto questionDetail, LoginUserDetail loginUserDetail) {
        QuestionOverview question = questionDetail.getQuestion();
        if(!question.isHidden())
            return false;
        return loginUserDetail == null || !question.getOwnerId().equals(loginUserDetail.getId());
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

    public static void initQuestionForm(Model model, ExperienceRepositoryView experienceRepositoryView, LoginUserDetail loginUserDetail) {
        model.addAttribute("question", new QuestionForm());
        model.addAttribute("experienceList", experienceRepositoryView.findExperienceList(loginUserDetail.getId()));
    }

    public static void initEditQuestionForm(Model model, QuestionForm form, ExperienceRepositoryView experienceRepositoryView, LoginUserDetail loginUserDetail) {
        model.addAttribute("question", form);
        model.addAttribute("experienceList", experienceRepositoryView.findExperienceList(loginUserDetail.getId()));
    }
}
