package com.mock.interview.interviewanswer.presentation.web;

import com.mock.interview.interviewanswer.domain.exception.InterviewAnswerNotFoundException;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepositoryForListView;
import com.mock.interview.interviewanswer.presentation.dto.AnswerDetailDto;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
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
    private final QuestionRepositoryForView questionRepositoryForView;
    @GetMapping("/question/{questionId}/answer")
    public String questionAnswerPage(
            Model model,
            @PathVariable("questionId") long questionId,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        Page<AnswerDetailDto> answerPage = interviewAnswerRepositoryForListView.findQuestionAnswerPage(questionId, pageable);
        AnswerPageInitializer.initListPage(model, questionId, answerPage, request);
        return "answer/list";
    }

    @GetMapping("/question/{questionId}/answer/{answerId}")
    public String answerDetailPage(
            Model model,
            @PathVariable("questionId") long questionId,
            @PathVariable("answerId") long answerId
    ) {
        AnswerDetailDto answer = interviewAnswerRepositoryForListView.findAnswer(answerId, questionId);
        if(answer == null)
            throw new InterviewAnswerNotFoundException();

        QuestionOverview question = questionRepositoryForView.findQuestionOverview(questionId);

        QuestionPageInitializer.initQuestionOverview(model, question);
        AnswerPageInitializer.initAnswerDetail(model, answer);
        AnswerPageInitializer.initAnswerPageHeaderSelected(model);
        return "answer/detail";
    }
}
