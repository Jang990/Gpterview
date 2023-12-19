package com.mock.interview.presentaion.web;

import com.mock.interview.domain.Category;
import com.mock.interview.presentaion.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SpeechController {

    @GetMapping("/interview/start")
    public String startInterviewPage(
            Model model,
            CandidateProfileDto profile,
            InterviewDetailsDto interviewDetails
    ) {
        // TODO: interviewDetails에 시간으로 Redis로 만료시간 설정할 것.
        InterviewSettingDto interviewSettingDto = new InterviewSettingDto();
        interviewSettingDto.setProfile(profile);
        interviewSettingDto.setInterviewDetails(interviewDetails);
        MessageHistoryDto historyDTO = initHistory();

        InterviewRequestDTO interviewRequestDTO = new InterviewRequestDTO(interviewSettingDto, historyDTO);

        model.addAttribute("interviewInfo", interviewRequestDTO);
        return "interview/interview-start";
    }

    private MessageHistoryDto initHistory() {
        MessageHistoryDto historyDTO = new MessageHistoryDto();
        historyDTO.getMessages().add(new MessageDto(InterviewRole.INTERVIEWER.toString(), "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?"));
        historyDTO.getMessages().add(new MessageDto(InterviewRole.USER.toString(), "네. 준비됐습니다."));
        return historyDTO;
    }

    @GetMapping("/interview/setting")
    public String speechPage(Model model) {
        // TODO: Setting DTO를 만들고 Model에 넣어서 타임리프에 th:object로 연결할 것.
        model.addAttribute("categoryList", List.of(Category.IT));
        model.addAttribute("candidateProfile", new CandidateProfileDto());
        model.addAttribute("interviewDetails", new InterviewDetailsDto());

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
