package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewanswer.infra.InterviewAnswerRepositoryForView;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForView;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.ChildQuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("question")
    public String questionListPage(Model model, @PageableDefault Pageable pageable) {
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(null, null, null, pageable);
        model.addAttribute("headerActiveTap", "interview-question");
        model.addAttribute("questionPage", overviewPage);
        return "/question/list";
    }

    @GetMapping("question/{questionId}")
    public String questionDetailPage(
            Model model,
//            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "questionId") long questionId
    ) {
        Long loginId = null;
        model.addAttribute("headerActiveTap", "interview-question");
        QuestionOverview question = questionRepositoryForView.findQuestion(loginId, questionId);
        List<AnswerForView> answerTop3 = answerRepositoryForView.findAnswerTop3Likes(questionId);
        List<ChildQuestionOverview> childQuestionTop3 = questionRepositoryForView.findChildQuestionTop3Likes(questionId);

        model.addAttribute("question", question);
        model.addAttribute("answerTop3", answerTop3);
        model.addAttribute("childQuestion", childQuestionTop3);
        return "/question/detail";
    }
}
