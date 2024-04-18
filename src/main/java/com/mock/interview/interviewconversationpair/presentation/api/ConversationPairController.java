package com.mock.interview.interviewconversationpair.presentation.api;

import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationIds;
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

    @PostMapping("/recommendation")
    public ResponseEntity<Void> recommendAnotherQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        InterviewConversationIds conversationDto = new InterviewConversationIds(interviewId, loginId, pairId);
        conversationPairService.recommendAnotherQuestion(conversationDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/recommendation/ai")
    public ResponseEntity<Void> recommendAiQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        InterviewConversationIds conversationDto = new InterviewConversationIds(interviewId, loginId, pairId);
        conversationPairService.recommendAiQuestion(conversationDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/question/connection/{questionId}")
    public ResponseEntity<Void> connectQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId,
            @PathVariable(name = "questionId") long questionId
    ) {
        InterviewConversationIds conversationDto = new InterviewConversationIds(interviewId, loginId, pairId);
        conversationPairService.connectQuestion(conversationDto, questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
