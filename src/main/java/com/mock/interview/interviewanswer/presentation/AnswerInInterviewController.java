package com.mock.interview.interviewanswer.presentation;

import com.mock.interview.interview.presentation.dto.message.MessageDto;
import com.mock.interview.interviewanswer.application.InterviewAnswerInInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerInInterviewController {
    private final InterviewAnswerInInterviewService interviewAnswerInInterviewService;

    @PostMapping("/interview/{interviewId}/conversation/pair/{pairId}/answer")
    public ResponseEntity<Void> requestAiResponse(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId,
            @RequestBody MessageDto answer // TODO: Request로 이름 변경
    ) {
        interviewAnswerInInterviewService.create(loginId, interviewId, pairId, answer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
