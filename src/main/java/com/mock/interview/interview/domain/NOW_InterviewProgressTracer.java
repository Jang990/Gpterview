package com.mock.interview.interview.domain;

import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.InterviewPhases;
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
    private static final InterviewPhase[] COMPOSITE_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE, InterviewPhase.PERSONAL};
    private static final InterviewPhase[] TECH_EX_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] TECH_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.TECHNICAL};
    private static final InterviewPhase[] EXPERIENCE_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] PERSONAL_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.PERSONAL};

    static InterviewPhase[] getPhaseOrder(InterviewType type) {
        return getOriginalPhase(type).clone();
    }

    private static InterviewPhase[] getOriginalPhase(InterviewType type) {
        return switch (type) {
            case TECHNICAL -> TECH_PHASE_ORDER;
            case PERSONALITY -> PERSONAL_PHASE_ORDER;
            case EXPERIENCE -> EXPERIENCE_PHASE_ORDER;
            case TECHNICAL_EXPERIENCE -> TECH_EX_PHASE_ORDER;
            case COMPOSITE -> COMPOSITE_PHASE_ORDER;
        };
    }

    static int numberOfPhase(InterviewType interviewType) {
        return switch (interviewType) {
            case TECHNICAL, PERSONALITY, EXPERIENCE -> 1;
            case TECHNICAL_EXPERIENCE -> 2;
            case COMPOSITE -> 3;
        };
    }

    public InterviewPhase tracePhase(LocalDateTime now, InterviewTimer timer, InterviewType type) {
        validateExpiredTimer(now, timer);

        int currentIdx = findCurrentPhaseIdx(now, timer, type);
        return getPhaseOrder(type)[currentIdx];
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
        return interviewDuration(timer) / numberOfPhase(type);
    }

    private long interviewDuration(InterviewTimer timer) {
        return timePassed(timer.getStartedAt(), timer.getExpiredAt());
    }

    private long timePassed(LocalDateTime start, LocalDateTime end) {
        return TimeDifferenceCalculator.calculate(ChronoUnit.SECONDS, start, end);
    }
}
