package com.mock.interview.interviewconversationpair.presentation.api;

import com.mock.interview.interviewconversationpair.application.ConversationDetailService;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationRepositoryForView;
import com.mock.interview.interviewconversationpair.presentation.dto.ConversationContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConversationQuestionController {
    private final ConversationDetailService conversationDetailService;

    @GetMapping("/api/interview/{interviewId}/conversation/pair/{pairId}")
    public ResponseEntity<ConversationContentDto> findConversationQuestion(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "pairId") long pairId
    ) {
        return ResponseEntity.ok(conversationDetailService.findConversation(loginId, interviewId, pairId));
    }

}
