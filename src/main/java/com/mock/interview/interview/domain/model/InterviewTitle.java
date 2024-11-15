package com.mock.interview.interview.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTitle {
    private String title;

    protected InterviewTitle(String category, String position) {
        this.title = String.format("%s-%s 면접", category, position);
    }
}
