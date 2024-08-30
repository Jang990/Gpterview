package com.mock.interview.interview.infra.progress;

import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/** 진행 시간을 기반으로 현재 면접 진행 정도를 파악. */
@Component
@Getter
public class InterviewProgressTimeBasedTracker {

    private static final ChronoUnit BASE_TIME_UNIT = ChronoUnit.SECONDS;

    private static final InterviewPhase[] COMPOSITE_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE, InterviewPhase.PERSONAL};
    private static final InterviewPhase[] TECH_EX_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] TECH_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.TECHNICAL};
    private static final InterviewPhase[] EXPERIENCE_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.EXPERIENCE};
    private static final InterviewPhase[] PERSONAL_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.PERSONAL};

    public static InterviewPhase[] phaseOrder(InterviewConfig config) {
        return getPhase(config.type()).clone();
    }

    /** 현재 어떤 스테이지를 진행중인지 계산 */
    public InterviewPhase tracePhase(LocalDateTime now, InterviewConfig config) {
        validateConfig(now, config);

        if(isSinglePhase(phaseOrder(config)))
            return firstPhase(config);

        if(findCurrentPhaseIdx(now, config) < phaseOrderLength(config))
            return currentPhase(now, config);
        return lastPhase(config);
    }

    private boolean isAlreadyExpiredConfig(LocalDateTime now, InterviewConfig config) {
        return now.isAfter(config.expiredTime());
    }

    /** 현재 페이즈에서 경과된 시간 / 각 페이즈 시간 = ex) 0.24 */
    public double traceProgress(LocalDateTime now, InterviewConfig config) {
        validateConfig(now, config);

        return (double) currentPhaseElapsed(now, config) / eachPhaseDuration(config);
    }

    private void validateConfig(LocalDateTime now, InterviewConfig config) {
        if(isZeroDurationConfig(config))
            throw new IllegalArgumentException("잘못된 InterviewConfig(시작시간 == 만료시간)");
        if(isAlreadyExpiredConfig(now, config))
            throw new IllegalArgumentException("이미 만료된 InterviewConfig(만료시간 < 현재 시간)");
    }

    /** 경과 시간 / 각 페이즈 시간 */
    private int findCurrentPhaseIdx(LocalDateTime now, InterviewConfig config) {
        return (int) (elapsedDuration(config, now) / eachPhaseDuration(config));
    }

    private InterviewPhase currentPhase(LocalDateTime now, InterviewConfig config) {
        return phaseOrder(config)[findCurrentPhaseIdx(now, config)];
    }

    private InterviewPhase firstPhase(InterviewConfig config) {
        return phaseOrder(config)[0];
    }

    private int phaseOrderLength(InterviewConfig config) {
        return phaseOrder(config).length;
    }

    private long currentPhaseElapsed(LocalDateTime now, InterviewConfig config) {
        return elapsedDuration(config, now) % eachPhaseDuration(config);
    }

    /** 해당 면접 타입에 몇 개의 스테이지가 있는지 */
    private int numberOfPhase(InterviewType interviewType) {
        return switch (interviewType) {
            case TECHNICAL, PERSONALITY, EXPERIENCE -> 1;
            case TECHNICAL_EXPERIENCE -> 2;
            case COMPOSITE -> 3;
        };
    }

    private static boolean isSinglePhase(InterviewPhase[] phaseOrders) {
        return phaseOrders.length == 1;
    }

    /** 면접_총_시간 / 면접_페이즈_수 */
    private long eachPhaseDuration(InterviewConfig config) {
        return interviewDuration(config) / numberOfPhase(config.type());
    }

    private boolean isZeroDurationConfig(InterviewConfig config) {
        return interviewDuration(config) == 0;
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

    private InterviewPhase lastPhase(InterviewConfig config) {
        return phaseOrder(config)[phaseOrderLength(config) - 1];
    }

    private long interviewDuration(InterviewConfig config) {
        return timeDifference(config.startTime(), config.expiredTime());
    }

    private long elapsedDuration(InterviewConfig config, LocalDateTime now) {
        return timeDifference(config.startTime(), now);
    }

    /** base - target */
    private long timeDifference(LocalDateTime base, LocalDateTime target) {
        long result = TimeDifferenceCalculator.calculate(BASE_TIME_UNIT, base, target);
        if(result < 0)
            throw new IllegalArgumentException("면접 시간이 0보다 작음");
        return result;
    }
}
