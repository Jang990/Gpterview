package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForm;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.ParentQuestionSummaryDto;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AnswerFormWebController {
    private final QuestionRepositoryForView questionRepositoryForView;

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
}
