package com.mock.interview.user.presentation.web;

import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.infra.InterviewRepositoryForView;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.review.presentation.dto.ReviewIndexPageFragment;
import com.mock.interview.user.application.UserService;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.dto.AccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final InterviewService interviewService;
    private final InterviewRepositoryForView interviewRepositoryForView;

    @GetMapping("auth/login")
    public String loginPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/auth/login";
    }

    @GetMapping("auth/sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("account", new AccountDto());
        return "/auth/sign-up";
    }

    @GetMapping("/")
    public String indexPage(Model model, @AuthenticationPrincipal Users users) {
        if (users == null) {
            model.addAttribute("activeInterview", new InterviewResponse());
            model.addAttribute("interviewOverviewList", new ArrayList<>());
            model.addAttribute("reviewOverviewList", new ArrayList<>());
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

        return "index";
    }

    private InterviewResponse getActiveInterview(long loginId) {
        return interviewService.findActiveInterview(loginId)
                .orElseGet(InterviewResponse::new);
    }

    @GetMapping("/auth/{username}")
    public String myPage(@PathVariable(value = "username") String username) {
        // TODO: 세션정보와 일치하는지 확인할 것
        return "/auth/my-page";
    }

    @PostMapping("auth/sign-up")
    public String signUp(@Valid @ModelAttribute("account") AccountDto accountDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "auth/sign-up";

        userService.create(accountDto);
        return "redirect:/auth/login";
    }
}
