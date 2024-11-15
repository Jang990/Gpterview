package com.mock.interview.interview.domain.model;

public record ProgressPercent(double progress) {
    public ProgressPercent {
        if(progress < 0.0 || 1.0 < progress)
            throw new IllegalArgumentException("진행도 범위는 0 ~ 0.9999..");
    }
}
