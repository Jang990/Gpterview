package com.mock.interview.review.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewQuestionCommandController {
    @PostMapping("/api/interview/{interviewId}/conversation/{conversationId}/review")
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "interviewId") long interviewId,
            @PathVariable(name = "conversationId") long conversationId
    ) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/api/review/{reviewId}/memo")
    public ResponseEntity<Void> updateMemo(
            @AuthenticationPrincipal(expression = "id") Long loginId,
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "reviewId") long reviewId
    ) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
