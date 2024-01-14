package com.mock.interview.interview.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTitle {
    private String title;

    InterviewTitle(String department, String appliedJob) {
        this.title = String.format("%s-%s 면접", department, appliedJob);
    }
}
