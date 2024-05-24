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
public class TechQuestionListWebController {
    private final QuestionRepositoryForListView questionRepositoryForListView;

    @GetMapping("tech/{techId}/question")
    public String userQuestionListPage(
            Model model,
            @PathVariable(name = "techId") long techId,
            QuestionSearchCond searchCond,
            @PageableDefault Pageable pageable,
            HttpServletRequest request
    ) {
        QuestionSearchOptionsDto searchOptionsDto = QuestionSearchOptionsDto.builder()
                .techIdCond(techId)
                .searchCond(searchCond).build();
        Page<QuestionOverview> overviewPage = questionRepositoryForListView.findOverviewList(searchOptionsDto, pageable);
        QuestionPageInitializer.initListPage(model, overviewPage, searchCond, request);
        model.addAttribute("headerActiveTap", "user-question");
        return "question/list";
    }
}
