package com.mock.interview.review.presentation.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Controller
@RequiredArgsConstructor
public class ReviewController {
    @GetMapping("/users/{userId}/review")
    public String reviewListPage(
            @AuthenticationPrincipal(expression = "id") long loginId,
            @PathVariable(name = "userId") long userId
    ) {
        return "redirect:/";
    }
}
