package com.mock.interview.interview.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTimer {

    private int durationMinutes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    protected InterviewTimer(
            int durationMinutes,
            LocalDateTime startedAt,
            LocalDateTime expiredAt) {
        this.durationMinutes = durationMinutes;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
    }

    InterviewTimer withExpiredAt(LocalDateTime expiredAt) {
        return new InterviewTimer(this.durationMinutes, this.startedAt, expiredAt);
    }

    boolean isExpired(LocalDateTime base) {
        return expiredAt.equals(base)|| expiredAt.isBefore(base);
    }
}
