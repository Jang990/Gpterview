package com.mock.interview.presentaion.web;

import com.mock.interview.domain.Category;
import com.mock.interview.presentaion.web.dto.CandidateProfileDTO;
import com.mock.interview.presentaion.web.dto.InterviewInfo;
import com.mock.interview.infrastructure.gpt.ChatGPTRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SpeechController {

    private final ChatGPTRequester requester;

    @GetMapping("/interview/start")
    public String startInterviewPage(Model model) {
        // TODO: testCreator를 지우고 /setting에서 넘어오면서 받은 DTO를 사용할 것
        InterviewInfo interviewInfo = InterviewInfo.testCreator("백엔드", "개발");
//        Message message = requester.sendRequest(interviewInfo);
//        interviewInfo.getMessageHistory().getMessages().add(message);
        model.addAttribute("interviewInfo", interviewInfo);
        System.out.println(interviewInfo);
        return "interview/interview-start";
    }

    @GetMapping("/interview/setting")
    public String speechPage(Model model) {
        // TODO: Setting DTO를 만들고 Model에 넣어서 타임리프에 th:object로 연결할 것.
        model.addAttribute("categoryList", List.of(Category.IT));
        model.addAttribute("candidateProfile", new CandidateProfileDTO());

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
