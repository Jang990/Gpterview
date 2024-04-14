package com.mock.interview.interviewconversationpair.presentation.api;

import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationLockDto;
import com.mock.interview.interviewconversationpair.application.ConversationPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview/{interviewId}/conversation/pair/{pairId}")
@RequiredArgsConstructor
public class ConversationPairController {
    private final ConversationPairService conversationPairService;

    @PostMapping("/recommendation/another")
    public ResponseEntity<Void> recommendAnotherQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        InterviewConversationLockDto conversationDto = new InterviewConversationLockDto(interviewId, loginId, pairId);
        conversationPairService.recommendAnotherQuestion(conversationDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/question/connection/{questionId}")
    public ResponseEntity<Void> connectQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId,
            @PathVariable(name = "questionId") long questionId
    ) {
        InterviewConversationLockDto conversationDto = new InterviewConversationLockDto(interviewId, loginId, pairId);
        conversationPairService.connectQuestion(conversationDto, questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
