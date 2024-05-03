package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewanswer.application.AnswerService;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepositoryForListView;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForm;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.ParentQuestionSummaryDto;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AnswerFormWebController {
    private final QuestionRepositoryForView questionRepositoryForView;
    private final InterviewAnswerRepositoryForListView interviewAnswerRepositoryForListView;
    private final AnswerService answerService;

    @GetMapping("/question/{questionId}/answer/form")
    public String answerFormPage(
            Model model,
            @PathVariable("questionId") long questionId,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail
    ) {
        ParentQuestionSummaryDto questionSummary = questionRepositoryForView.findQuestionSummary(questionId);
        if(QuestionPageInitializer.isUnauthorized(loginUserDetail, questionSummary))
            return "redirect:/question/"+questionId+"/unauthorized";
        model.addAttribute("question", questionSummary);
        model.addAttribute("answer", new AnswerForm());
        return "/answer/form";
    }

    @GetMapping("/question/{questionId}/answer/{answerId}/edit/form")
    public String answerFormPage(
            Model model,
            @PathVariable("questionId") long questionId,
            @PathVariable("answerId") long answerId,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail
    ) {
        QuestionOverview question = questionRepositoryForView.findQuestionOverview(questionId);
        if(QuestionPageInitializer.isUnauthorized(loginUserDetail, question))
            return "redirect:/question/"+questionId+"/unauthorized";
        model.addAttribute("question", question);


        AnswerForm answerEditForm = interviewAnswerRepositoryForListView.findAnswerEditForm(answerId, loginUserDetail.getId());
        model.addAttribute("answer", answerEditForm);
        return "/answer/edit-form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/{questionId}/answer")
    public String saveAnswer(
            @PathVariable("questionId") long questionId,
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            AnswerForm answerForm
    ) {
        long answerId = answerService.saveAnswer(questionId, loginUserDetail.getId(), answerForm);
        System.out.println("redirect:/question/" + questionId + "/answer/" + answerId);
        return "redirect:/question/" + questionId + "/answer/" + answerId;
    }
}
