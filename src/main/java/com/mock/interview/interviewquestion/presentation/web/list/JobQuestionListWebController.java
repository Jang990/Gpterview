package com.mock.interview.interviewquestion.presentation.web.list;

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
public class JobQuestionListWebController {
    private final QuestionRepositoryForListView questionRepositoryForListView;

    @GetMapping("question/category/{categoryId}")
    public String categoryQuestionListPage(
            @PathVariable(name = "categoryId") long categoryId,
            QuestionSearchCond searchCond,
            Model model, @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder()
                .categoryIdCond(categoryId)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForListView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "question/list";
    }

    @GetMapping("question/position/{positionId}")
    public String positionQuestionListPage(
            @PathVariable(name = "positionId") long positionId,
            QuestionSearchCond searchCond,
            Model model, @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptions = QuestionSearchOptionsDto.builder()
                .positionIdCond(positionId)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForListView.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        return "question/list";
    }
}
