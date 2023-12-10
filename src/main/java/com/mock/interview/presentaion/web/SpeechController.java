package com.mock.interview.presentaion.web;

import com.mock.interview.domain.Category;
import com.mock.interview.presentaion.web.dto.*;
import com.mock.interview.infrastructure.gpt.ChatGPTRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SpeechController {

//    private final AIService service;

    @GetMapping("/interview/start")
    public String startInterviewPage(
            Model model,
            CandidateProfileDTO profile,
            InterviewDetailsDTO interviewDetails
    ) {
        InterviewInfo interviewInfo = new InterviewInfo();
        interviewInfo.setProfile(profile);
        interviewInfo.setInterviewDetails(interviewDetails);
//        Message message = service.service(interviewInfo);
        Message message = new Message(InterviewRole.INTERVIEWER.getName(), "Hello World!");
        interviewInfo.getMessageHistory().getMessages().add(message);
        model.addAttribute("interviewInfo", interviewInfo);

        return "interview/interview-start";
    }

    @GetMapping("/interview/setting")
    public String speechPage(Model model) {
        // TODO: Setting DTO를 만들고 Model에 넣어서 타임리프에 th:object로 연결할 것.
        model.addAttribute("categoryList", List.of(Category.IT));
        model.addAttribute("candidateProfile", new CandidateProfileDTO());
        model.addAttribute("interviewDetails", new InterviewDetailsDTO());

        // TODO: Ajax로 분야에 맞는 필드를 가져오도록 수정해야 한다. 일단 임시로 each문 돌린다.
        // TODO: 지금은 면접 설정은 생략하고 기술면접으로 진행한다. 추후 면접 설정도 추가해야 한다.
        return "interview/interview-setting";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("auth/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("auth/sign-up")
    public String signUp() {
        return "/auth/sign-up";
    }
}
