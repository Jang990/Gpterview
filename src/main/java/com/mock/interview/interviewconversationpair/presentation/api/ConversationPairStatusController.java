package com.mock.interview.interviewconversationpair.presentation.api;

import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationLockDto;
import com.mock.interview.interviewconversationpair.application.PairStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview/{interviewId}/conversation/pair/{pairId}")
@RequiredArgsConstructor
public class ConversationPairStatusController {

    private final PairStatusService pairStatusService;

    @PostMapping("/status/changing")
    public ResponseEntity<Void> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        InterviewConversationLockDto conversationDto = new InterviewConversationLockDto(interviewId, loginId, pairId);
        pairStatusService.changeQuestionTopic(conversationDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/status/ai")
    public ResponseEntity<Void> requestAi(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        InterviewConversationLockDto conversationDto = new InterviewConversationLockDto(interviewId, loginId, pairId);
        pairStatusService.changeRequestingAi(conversationDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
