package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.interviewanswer.infra.InterviewAnswerRepositoryForListView;
import com.mock.interview.interviewanswer.presentation.dto.AnswerForView;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AnswerWebController {
    private final InterviewAnswerRepositoryForListView interviewAnswerRepositoryForListView;
    @GetMapping("/question/{questionId}/answer")
    public String questionAnswerPage(
            Model model,
            @PathVariable("questionId") long questionId,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        Page<AnswerForView> answerPage = interviewAnswerRepositoryForListView.findQuestionAnswerPage(questionId, pageable);
        AnswerPageInitializer.initListPage(model, questionId, answerPage, request);
        return "/answer/list";
    }
}
