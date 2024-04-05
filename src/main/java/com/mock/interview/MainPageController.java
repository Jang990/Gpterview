package com.mock.interview;

import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.infra.InterviewRepositoryForView;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.interviewquestion.presentation.web.QuestionPageInitializer;
import com.mock.interview.review.presentation.dto.ReviewIndexPageFragment;
import com.mock.interview.user.domain.model.Users;
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
    private final InterviewService interviewService;
    private final InterviewRepositoryForView interviewRepositoryForView;

    @GetMapping("/")
    public String indexPage(Model model, @AuthenticationPrincipal Users users) {
        if (users == null) {
            model.addAttribute("activeInterview", new InterviewResponse());
            model.addAttribute("interviewOverviewList", new ArrayList<>());
            model.addAttribute("reviewOverviewList", new ArrayList<>());
            QuestionPageInitializer.initEmptyQuestionSearchForm(model);
            return "index";
        }

        final int maxOverviewListSize = 3;
        List<InterviewOverviewFragment> interviewOverviewList = interviewRepositoryForView
                .findInterviewOverview(users.getId(), PageRequest.of(0, maxOverviewListSize));

        // TODO: 개발 후 제대로 뺄 것.
        List<ReviewIndexPageFragment> reviewOverviewList =
                List.of(
                        new ReviewIndexPageFragment(1, "멀티 프로세싱과 멀티스레딩을 어떤 상황에서 사용하는 것이 적절할까요?", 10),
                        new ReviewIndexPageFragment(3, "FIFO 방식의 스케줄링과 라운드 로빈 방식의 스케줄링을 비교해보세요.", 6)
                );

        model.addAttribute("activeInterview", getActiveInterview(users.getId()));
        model.addAttribute("interviewOverviewList", interviewOverviewList);
        model.addAttribute("reviewOverviewList", reviewOverviewList);
        QuestionPageInitializer.initEmptyQuestionSearchForm(model);

        return "index";
    }

    private InterviewResponse getActiveInterview(long loginId) {
        return interviewService.findActiveInterview(loginId)
                .orElseGet(InterviewResponse::new);
    }
}
