package com.mock.interview.interview.infra.progress;

import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
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

    public final InterviewPhase[] COMPOSITE_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE, InterviewPhase.PERSONAL};
    private final InterviewPhase[] TECH_EX_PHASE_ORDER = {InterviewPhase.TECHNICAL, InterviewPhase.EXPERIENCE};
    private final InterviewPhase[] TECH_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.TECHNICAL};
    private final InterviewPhase[] EXPERIENCE_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.EXPERIENCE};
    private final InterviewPhase[] PERSONAL_PHASE_ORDER = new InterviewPhase[]{InterviewPhase.PERSONAL};

    public InterviewPhase[] getPhaseOrder(InterviewType type) {
        return getPhase(type).clone();
    }

    public InterviewProgress getCurrentInterviewProgress(InterviewConfig config) {
        InterviewType interviewType = config.interviewType();
        LocalDateTime now = LocalDateTime.now();

        InterviewPhase interviewPhase = getCurrentPhase(now, interviewType, config);
        double progress = computePhaseProgress(now, interviewType, config);

        return new InterviewProgress(interviewPhase, progress);
    }

    /** 해당 면접 타입에 몇 개의 스테이지가 있는지 */
    private int getNumberOfPhase(InterviewType interviewType) {
        return switch (interviewType) {
            case TECHNICAL, PERSONALITY, EXPERIENCE -> 1;
            case TECHNICAL_EXPERIENCE -> 2;
            case COMPOSITE -> 3;
        };
    }

    /** Phase 진행도 백분률 계산 */
    private double computePhaseProgress(LocalDateTime now, InterviewType type, InterviewConfig config) {
        long eachPhaseSecond = getEachPhaseSecond(type, config);
        long elapsedSecond = getSecondDifference(config.startTime(), now);
        long aa = elapsedSecond % eachPhaseSecond;
        return (double) aa / eachPhaseSecond;
    }

    /** 현재 어떤 스테이지를 진행중인지 계산 */
    private InterviewPhase getCurrentPhase(LocalDateTime now, InterviewType type, InterviewConfig config) {
        InterviewPhase[] phaseOrders = getPhase(type);
        if(isSinglePhase(phaseOrders))
            return phaseOrders[0];

        long eachPhaseSecond = getEachPhaseSecond(type, config);
        long elapsedSecond = getSecondDifference(config.startTime(), now);
        int phaseIdx = (int) (elapsedSecond / eachPhaseSecond);

        if(phaseIdx < phaseOrders.length)
            return phaseOrders[ phaseIdx];
        return lastPhase(phaseOrders);
    }

    private static boolean isSinglePhase(InterviewPhase[] phaseOrders) {
        return phaseOrders.length == 1;
    }

    /** 면접_총_시간 / 면접_페이즈_수 */
    private long getEachPhaseSecond(InterviewType type, InterviewConfig config) {
        long interviewDurationSecond = getSecondDifference(config.startTime(), config.expiredTime());
        if(interviewDurationSecond <= 0)
            throw new IllegalArgumentException("면접 시간이 0보다 작음"); // 비즈니스 로직상은 불가능 - DB 직접 변경 시 발생 가능

        int numberOfPhase = getNumberOfPhase(type);
        return interviewDurationSecond / numberOfPhase;
    }

    private InterviewPhase[] getPhase(InterviewType type) {
        return switch (type) {
            case TECHNICAL -> TECH_PHASE_ORDER;
            case PERSONALITY -> PERSONAL_PHASE_ORDER;
            case EXPERIENCE -> EXPERIENCE_PHASE_ORDER;
            case TECHNICAL_EXPERIENCE -> TECH_EX_PHASE_ORDER;
            case COMPOSITE -> COMPOSITE_PHASE_ORDER;
        };
    }

    private InterviewPhase lastPhase(InterviewPhase[] phaseOrders) {
        return phaseOrders[phaseOrders.length - 1];
    }

    /** base - target (Minute 단위) */
    private long getSecondDifference(LocalDateTime base, LocalDateTime target) {
        long diffSecond = ChronoUnit.SECONDS.between(base, target);
        if(diffSecond < 0)
            throw new IllegalArgumentException("base < target");
        return diffSecond;
    }
}
