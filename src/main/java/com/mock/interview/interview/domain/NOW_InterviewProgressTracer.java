package com.mock.interview.interview.domain;

import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.presentation.dto.InterviewType;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/** 진행 시간을 기반으로 현재 면접 진행 정도를 파악. */
@Service
@Getter
public class NOW_InterviewProgressTracer {

    private static final ChronoUnit BASE_TIME_UNIT = ChronoUnit.SECONDS;

    private static final InterviewPhase[] COMPOSITE_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE, InterviewPhase.PERSONAL};
    private static final InterviewPhase[] TECH_EX_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] TECH_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.TECHNICAL};
    private static final InterviewPhase[] EXPERIENCE_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] PERSONAL_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.PERSONAL};

    public static InterviewPhase[] phaseOrder(InterviewType type) {
        return getPhase(type).clone();
    }

    private static InterviewPhase[] getPhase(InterviewType type) {
        return switch (type) {
            case TECHNICAL -> TECH_PHASE_ORDER;
            case PERSONALITY -> PERSONAL_PHASE_ORDER;
            case EXPERIENCE -> EXPERIENCE_PHASE_ORDER;
            case TECHNICAL_EXPERIENCE -> TECH_EX_PHASE_ORDER;
            case COMPOSITE -> COMPOSITE_PHASE_ORDER;
        };
    }

    /** 현재 어떤 스테이지를 진행중인지 계산 */
    public InterviewPhase tracePhase(LocalDateTime now, Interview interview) {
        validateTimer(now, interview);
        return phaseOrder(interview.getType())[findCurrentPhaseIdx(now, interview)];
    }

    /** 현재 페이즈에서 경과된 시간 / 각 페이즈 시간 = ex) 0.24 */
    public double traceProgress(LocalDateTime now, Interview interview) {
        validateTimer(now, interview);
        return (double) currentPhaseElapsed(now, interview) / eachPhaseDuration(interview);
    }

    private void validateTimer(LocalDateTime now, Interview interview) {
        if(isAlreadyExpiredConfig(now, interview))
            throw new IsAlreadyTimeoutInterviewException();
    }

    private boolean isAlreadyExpiredConfig(LocalDateTime now, Interview interview) {
        return now.isEqual(interview.getTimer().getExpiredAt()) || now.isAfter(interview.getTimer().getExpiredAt());
    }

    /** 경과 시간 / 각 페이즈 시간 */
    private int findCurrentPhaseIdx(LocalDateTime now, Interview interview) {
        return (int) (elapsedDuration(interview, now) / eachPhaseDuration(interview));
    }

    private long currentPhaseElapsed(LocalDateTime now, Interview interview) {
        return elapsedDuration(interview, now) % eachPhaseDuration(interview);
    }

    /** 해당 면접 타입에 몇 개의 스테이지가 있는지 */
    private int numberOfPhase(InterviewType interviewType) {
        return switch (interviewType) {
            case TECHNICAL, PERSONALITY, EXPERIENCE -> 1;
            case TECHNICAL_EXPERIENCE -> 2;
            case COMPOSITE -> 3;
        };
    }

    /** 면접_총_시간 / 면접_페이즈_수 */
    private long eachPhaseDuration(Interview interview) {
        return interviewDuration(interview) / numberOfPhase(interview.getType());
    }

    private long interviewDuration(Interview interview) {
        return timeDifference(interview.getTimer().getStartedAt(), interview.getTimer().getExpiredAt());
    }

    private long elapsedDuration(Interview interview, LocalDateTime now) {
        return timeDifference(interview.getTimer().getStartedAt(), now);
    }

    /** base - target */
    private long timeDifference(LocalDateTime base, LocalDateTime target) {
        long result = TimeDifferenceCalculator.calculate(BASE_TIME_UNIT, base, target);
        if(result < 0)
            throw new IllegalArgumentException("면접 시간이 0보다 작음");
        return result;
    }
}
