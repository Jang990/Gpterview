package com.mock.interview.review.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewQuestionOverviewFragment {
    private long id;
    private String question;
    private int memoCount;
    private LocalDateTime createdAt;
}
