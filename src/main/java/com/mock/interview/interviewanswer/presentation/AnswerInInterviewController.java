package com.mock.interview.interviewanswer.presentation;

import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationLockDto;
import com.mock.interview.interviewanswer.presentation.dto.InterviewAnswerRequest;
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
            @RequestBody InterviewAnswerRequest answer
    ) {
        InterviewConversationLockDto conversationDto = new InterviewConversationLockDto(interviewId, loginId, pairId);
        interviewAnswerInInterviewService.create(conversationDto, answer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
