package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.*;
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
public class QuestionRelationWebController {
    private final QuestionRepositoryForView questionRepositoryForView;

    @GetMapping("question/{questionId}/child")
    public String childQuestionListPage(
            Model model,
            @PathVariable(name = "questionId") long questionId,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptionsDto = QuestionSearchOptionsDto.builder()
                .parentQuestionIdCond(questionId)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(searchOptionsDto, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "/question/list";
    }

    @GetMapping("question/form/parent")
    public String selectChildQuestionPage(
            Model model,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        // 임시 코드
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder().searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "/question/list";
    }
}
