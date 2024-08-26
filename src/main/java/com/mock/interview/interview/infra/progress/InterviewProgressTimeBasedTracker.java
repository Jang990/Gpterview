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
        if(isSinglePhase(phaseOrder(config)))
            return firstPhase(config);

        int phaseIdx = findPhaseIdx(now, config);

        if(phaseIdx < phaseOrderLength(config))
            return selectPhase(config, phaseIdx);
        return lastPhase(config);
    }

    private int findPhaseIdx(LocalDateTime now, InterviewConfig config) {
        long eachPhaseDuration = eachPhaseDuration(config);
        long elapsedDuration = timeDifference(config.startTime(), now);
        return (int) (elapsedDuration / eachPhaseDuration);
    }

    private InterviewPhase selectPhase(InterviewConfig config, int phaseIdx) {
        return phaseOrder(config)[phaseIdx];
    }

    private InterviewPhase firstPhase(InterviewConfig config) {
        return selectPhase(config, 0);
    }

    private int phaseOrderLength(InterviewConfig config) {
        return phaseOrder(config).length;
    }

    /** Phase 진행도 백분률 계산 */
    public double traceProgress(LocalDateTime now, InterviewConfig config) {
        long aa = timeDifference(config.startTime(), now) % eachPhaseDuration(config);
        return (double) aa / eachPhaseDuration(config);
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
        return selectPhase(config, phaseOrderLength(config) - 1);
    }

    private long interviewDuration(InterviewConfig config) {
        return timeDifference(config.startTime(), config.expiredTime());
    }

    /** base - target */
    private long timeDifference(LocalDateTime base, LocalDateTime target) {
        long result = TimeDifferenceCalculator.calculate(BASE_TIME_UNIT, base, target);
        if(result < 0)
            throw new IllegalArgumentException("면접 시간이 0보다 작음");
        return result;
    }
}
