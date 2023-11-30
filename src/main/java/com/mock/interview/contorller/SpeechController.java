package com.mock.interview.contorller;

import com.mock.interview.contorller.dto.InterviewInfo;
import com.mock.interview.contorller.dto.Message;
import com.mock.interview.gpt.ChatGPTRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SpeechController {

    private final ChatGPTRequester requester;

    @GetMapping("/interview/start")
    public String startInterviewPage(Model model) {
        // TODO: testCreator를 지우고 /setting에서 넘어오면서 받은 DTO를 사용할 것
        InterviewInfo interviewInfo = InterviewInfo.testCreator("백엔드", "개발");
        Message message = requester.sendRequest(interviewInfo);
        interviewInfo.getMessageHistory().getMessages().add(message);
        model.addAttribute("interviewInfo", interviewInfo);
        return "interview/interview-start";
    }

    @GetMapping("/interview/setting")
    public String speechPage() {
        // TODO: Setting DTO를 만들고 Model에 넣어서 타임리프에 th:object로 연결할 것.
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
