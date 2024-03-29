package com.mock.interview.interviewconversationpair.presentation.api;

import com.mock.interview.interviewconversationpair.application.ConversationPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConversationPairController {
    private final ConversationPairService conversationPairService;

    @PostMapping("/interview/{interviewId}/conversation/pair/{pairId}/changing-topic")
    public ResponseEntity<Void> changingTopic(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        conversationPairService.changeQuestionTopic(loginId, interviewId, pairId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/interview/{interviewId}/conversation/pair/{pairId}/question/connection/{questionId}")
    public ResponseEntity<Void> connectQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId,
            @PathVariable(name = "questionId") long questionId
    ) {

        conversationPairService.connectQuestion(loginId, interviewId, pairId, questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
