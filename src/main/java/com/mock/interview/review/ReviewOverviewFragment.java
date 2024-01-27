package com.mock.interview.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewOverviewFragment {
    private long id;
    private String content;
    private int memoCount;
}
