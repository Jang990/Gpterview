package com.mock.interview;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.interview.application.ActiveInterviewService;
import com.mock.interview.interview.infra.InterviewRepositoryForView;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final InterviewRepositoryForView interviewRepositoryForView;
    private final ActiveInterviewService activeInterviewService;

    @GetMapping("/")
    public String indexPage(Model model, @AuthenticationPrincipal LoginUserDetail users) {
        QuestionPageInitializer.initEmptyQuestionSearchForm(model);
        if (users == null) {
            model.addAttribute("activeInterview", new InterviewResponse());
            model.addAttribute("interviewOverviewList", new ArrayList<>());
            return "index";
        }

        final int maxOverviewListSize = 3;
        List<InterviewOverviewFragment> interviewOverviewList = interviewRepositoryForView
                .findInterviewOverview(users.getId(), PageRequest.of(0, maxOverviewListSize));

        model.addAttribute("activeInterview", getActiveInterview(users.getId()));
        model.addAttribute("interviewOverviewList", interviewOverviewList);
        return "index";
    }

    private InterviewResponse getActiveInterview(long loginId) {
        return activeInterviewService.find(loginId)
                .orElseGet(InterviewResponse::new);
    }
}
