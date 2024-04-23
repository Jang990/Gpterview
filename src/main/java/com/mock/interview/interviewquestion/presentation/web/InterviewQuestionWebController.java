package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewanswer.infra.InterviewAnswerRepositoryForView;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForView;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.ChildQuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.dto.UnauthorizedPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InterviewQuestionWebController {
    private final InterviewQuestionRepositoryForView questionRepositoryForView;
    private final InterviewAnswerRepositoryForView answerRepositoryForView;

    @GetMapping("/question/{questionId}/unauthorized")
    public String unauthorizedPage(Model model, @PathVariable(name = "questionId") long questionId) {
        model.addAttribute("info", new UnauthorizedPageInfo("접근 권한 없음", String.valueOf(questionId), "/question"));
        return "/question/unauthorized";
    }


    @GetMapping("/question/{questionId}")
    public String questionDetailPage(
            Model model,
            @AuthenticationPrincipal Users users,
            @PathVariable(name = "questionId") long questionId
    ) {
        model.addAttribute("headerActiveTap", "interview-question");
        QuestionOverview question = questionRepositoryForView.findQuestion(questionId);
        if (isUnauthorized(users, question)) {
            return "redirect:/question/"+questionId+"/unauthorized";
        }

        List<AnswerForView> answerTop3 = answerRepositoryForView.findAnswerTop3Likes(questionId);
        List<ChildQuestionOverview> childQuestionTop3 = questionRepositoryForView.findChildQuestionTop3Likes(questionId);

        model.addAttribute("question", question);
        model.addAttribute("answerTop3", answerTop3);
        model.addAttribute("childQuestion", childQuestionTop3);
        return "/question/detail";
    }

    private static boolean isUnauthorized(Users users, QuestionOverview question) {
        if(!question.isHidden())
            return false;

        return users == null || !users.getUsername().equals(question.getCreatedBy());
    }
}
