package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InterviewTimerCreator {
    private final InterviewTimeHolder timeHolder;

    // TODO: durationMinutes를 객체로.
    public InterviewTimer create(int durationMinutes) {
        if (durationMinutes <= 0 || 60 < durationMinutes)
            throw new IllegalArgumentException("면접 시간은 1분 이상 60분 이하로 설정");

        LocalDateTime now = timeHolder.now();
        return new InterviewTimer(
                durationMinutes, now,
                now.plusMinutes(durationMinutes)
        );
    }
}
