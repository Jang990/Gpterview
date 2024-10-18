package com.mock.interview.interview.domain;

import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.InterviewTimer;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.presentation.dto.InterviewType;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Getter
public class NOW_InterviewProgressTracer {

    public InterviewPhase tracePhase(LocalDateTime now, InterviewTimer timer, InterviewType type) {
        validateExpiredTimer(now, timer);

        int currentIdx = findCurrentPhaseIdx(now, timer, type);
        return InterviewPhases.getPhaseOrder(type)[currentIdx];
    }

    public double traceProgress(LocalDateTime now, InterviewTimer timer, InterviewType type) {
        validateExpiredTimer(now, timer);

        long eachPhaseDuration = eachPhaseDuration(timer, type);
        long passedTimeOfCurrentPhase = timePassed(timer.getStartedAt(), now) % eachPhaseDuration;
        return (double) passedTimeOfCurrentPhase / eachPhaseDuration;
    }

    private void validateExpiredTimer(LocalDateTime now, InterviewTimer timer) {
        if(timer.getExpiredAt().equals(now) || timer.getExpiredAt().isBefore(now))
            throw new IsAlreadyTimeoutInterviewException();
    }

    /** 경과 시간 / 각 페이즈 시간 */
    private int findCurrentPhaseIdx(LocalDateTime now, InterviewTimer timer, InterviewType type) {
        return (int) (timePassed(timer.getStartedAt(), now) / eachPhaseDuration(timer, type));
    }

    /** 면접_총_시간 / 면접_페이즈_수 */
    private long eachPhaseDuration(InterviewTimer timer, InterviewType type) {
        return interviewDuration(timer) / InterviewPhases.numberOfPhase(type);
    }

    private long interviewDuration(InterviewTimer timer) {
        return timePassed(timer.getStartedAt(), timer.getExpiredAt());
    }

    private long timePassed(LocalDateTime start, LocalDateTime end) {
        return TimeDifferenceCalculator.calculate(ChronoUnit.SECONDS, start, end);
    }
}
