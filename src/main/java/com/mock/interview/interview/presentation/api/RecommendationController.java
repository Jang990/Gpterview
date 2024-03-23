package com.mock.interview.interview.presentation.api;

import com.mock.interview.interview.application.RecommendedQuestionService;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendedQuestionService recommendedQuestionService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/interview/{interviewId}/question/recommendation")
    public ResponseEntity<Void> recommend(
            @PathVariable(name = "interviewId") long interviewId,
            @AuthenticationPrincipal(expression = "id") Long loginId
    ) {
        recommendedQuestionService.recommend(loginId, interviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
