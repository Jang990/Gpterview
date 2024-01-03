package com.mock.interview.interview.infrastructure.interview.setting;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InterviewSetting {
    private final String concept;
    protected InterviewSetting(String concept) {
        this.concept = concept;
    }
}