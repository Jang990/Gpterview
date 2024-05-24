package com.mock.interview.interviewquestion.presentation.web.list;

import com.mock.interview.interview.infra.InterviewQuestionRepositoryForView;
import com.mock.interview.interviewquestion.infra.QuestionRepositoryForListView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
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
public class QuestionListWebController {
    private final QuestionRepositoryForListView questionRepositoryForListView;
    private final InterviewQuestionRepositoryForView interviewQuestionRepositoryForView;

    @GetMapping("question")
    public String questionListPage(
            Model model,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder().searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForListView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "question/list";
    }

    /*@GetMapping("question/interview/{interviewId}")
    public String interviewQuestionListPage(
            Model model,
            @PathVariable("interviewId") long interviewId,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        Page<QuestionOverview> overviewPage = interviewQuestionRepositoryForView.findOverviewList(interviewId, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, new QuestionSearchCond(*//* 임시코드 *//*), request);
        return "interview/question";
    }*/

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
        Page<QuestionOverview> overviewPage = questionRepositoryForListView.findOverviewList(searchOptionsDto, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "question/list";
    }

    @GetMapping("question/users/{userId}")
    public String userQuestionListPage(
            Model model,
            @PathVariable(name = "userId") long userId,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptionsDto = QuestionSearchOptionsDto.builder()
                .ownerIdCond(userId)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForListView.findOverviewList(searchOptionsDto, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        model.addAttribute("headerActiveTap", "user-question");
        return "question/list";
    }
}
