package com.mock.interview.review.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewIndexPageFragment {
    private long id;
    private String content;
    private int memoCount;
}
