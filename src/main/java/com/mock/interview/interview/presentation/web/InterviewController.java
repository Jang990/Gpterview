package com.mock.interview.interview.presentation.web;

import com.mock.interview.interview.application.InterviewService;
import com.mock.interview.interview.presentation.dto.*;
import com.mock.interview.user.application.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class InterviewController {

    private final CandidateProfileService candidateProfileService;
    private final InterviewService interviewService;

    @PostMapping("/interview")
    public String startInterviewPage(
            Model model,
            CandidateProfileForm profile,
            InterviewDetailsDto interviewDetails,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        model.addAttribute("headerActiveTap", "interview");

        // TODO: interviewDetails에 시간으로 Redis로 만료시간 설정할 것.
        long candidateProfileId = candidateProfileService.create(profile, loginId);
        long interviewId = interviewService.create(loginId, candidateProfileId, interviewDetails);
        return "redirect:/interview/" + interviewId;
    }

    @GetMapping("/interview/{interviewId}")
    public String interviewPage(
            Model model,
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId
    ) {
        model.addAttribute("headerActiveTap", "interview");
        model.addAttribute("interviewId", interviewId);
        model.addAttribute("messageHistory", initHistory()); // TODO: 임시코드 - 만약 DB에 있다면 가져와야 한다.
        return "interview/interview-start";
    }

    private MessageHistoryDto initHistory() {
        MessageHistoryDto historyDTO = new MessageHistoryDto();
        historyDTO.getMessages().add(new MessageDto(InterviewRole.INTERVIEWER.toString(), "안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?"));
        historyDTO.getMessages().add(new MessageDto(InterviewRole.USER.toString(), "네. 준비됐습니다."));
        return historyDTO;
    }
}
