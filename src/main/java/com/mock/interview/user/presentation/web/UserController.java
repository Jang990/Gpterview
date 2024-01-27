package com.mock.interview.user.presentation.web;

import com.mock.interview.candidate.presentation.dto.InterviewType;
import com.mock.interview.global.exception.CustomClientException;
import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.presentation.dto.InterviewOverviewFragment;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import com.mock.interview.temp.ReviewOverviewFragment;
import com.mock.interview.user.application.UserService;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.presentation.dto.AccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final InterviewService interviewService;

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
        if(users != null)
            model.addAttribute("activeInterview", getActiveInterview(users.getId()));
        else
            model.addAttribute("activeInterview", new InterviewResponse());

        List<InterviewOverviewFragment> interviewOverviewList =
                List.of(
                        new InterviewOverviewFragment("IT-백엔드 면접", InterviewType.COMPOSITE, LocalDateTime.now()),
                        new InterviewOverviewFragment("IT-모바일 면접", InterviewType.TECHNICAL, LocalDateTime.now()),
                        new InterviewOverviewFragment("인사-헤드헌터 면접", InterviewType.TECHNICAL_EXPERIENCE, LocalDateTime.now())
                );

        List<ReviewOverviewFragment> reviewOverviewList =
                List.of(
                        new ReviewOverviewFragment(1, "멀티 프로세싱과 멀티스레딩을 어떤 상황에서 사용하는 것이 적절할까요?", 10),
                        new ReviewOverviewFragment(2, "멀티스레딩에서 뮤텍스와 세마포어의 차이점을 설명해주세요.", 2),
                        new ReviewOverviewFragment(3, "FIFO 방식의 스케줄링과 라운드 로빈 방식의 스케줄링을 비교해보세요.", 6)
                );

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
