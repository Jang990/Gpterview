package com.mock.interview.interviewquestion.presentation.web;

import com.mock.interview.interview.infra.InterviewQuestionRepositoryForView;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
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
public class QuestionSearchWebController {
    private final QuestionRepositoryForView questionRepositoryForView;
    private final InterviewQuestionRepositoryForView interviewQuestionRepositoryForView;

    @GetMapping("question")
    public String questionListPage(
            Model model,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder().searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "/question/list";
    }

    @GetMapping("/interview/{interviewId}/question")
    public String interviewQuestionListPage(
            Model model,
            @PathVariable("interviewId") long interviewId,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        Page<QuestionOverview> overviewPage = interviewQuestionRepositoryForView.findOverviewList(interviewId, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, new QuestionSearchCond(/* 임시코드 */), request);
        return "/interview/question";
    }

    @GetMapping("question/category/{categoryName}")
    public String categoryQuestionListPage(
            @PathVariable(name = "categoryName") String categoryName,
            QuestionSearchCond searchCond,
            Model model, @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder()
                .categoryNameCond(categoryName)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "/question/list";
    }

    @GetMapping("question/position/{positionName}")
    public String positionQuestionListPage(
            @PathVariable(name = "positionName") String positionName,
            QuestionSearchCond searchCond,
            Model model, @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder()
                .positionNameCond(positionName)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "/question/list";
    }
}
