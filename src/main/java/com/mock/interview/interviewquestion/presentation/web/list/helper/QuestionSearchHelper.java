package com.mock.interview.interviewquestion.presentation.web.list.helper;

import com.mock.interview.interviewquestion.infra.QuestionRepositoryForListView;
import com.mock.interview.interviewquestion.presentation.dto.QuestionOverview;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchCond;
import com.mock.interview.interviewquestion.presentation.dto.QuestionSearchOptionsDto;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public class QuestionSearchHelper {
    public static void search(
            QuestionRepositoryForListView repository,
            Model model, Pageable pageable,
            HttpServletRequest request,
            QuestionSearchOptionsDto searchOptions
    ) {
        Page<QuestionOverview> overviewPage = repository.findOverviewList(searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchOptions.getSearchCond(), request);
    }

    public static void searchFavoriteQuestion(
            QuestionRepositoryForListView repository,
            Model model, Pageable pageable,
            HttpServletRequest request,
            QuestionSearchOptionsDto searchOptions,
            long loginId
    ) {
        Page<QuestionOverview> overviewPage = repository.findFavoriteQuestionOverviewList(loginId, searchOptions, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchOptions.getSearchCond(), request);
    }
}
