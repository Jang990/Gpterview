package com.mock.interview.interviewquestion.infra.ai.progress;

import com.mock.interview.candidate.presentation.dto.InterviewType;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewConfig;
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

        InterviewPhase interviewPhase = computePhase(now, interviewType, config);
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
    private InterviewPhase computePhase(LocalDateTime now, InterviewType type, InterviewConfig config) {
        InterviewPhase[] phaseOrders = getPhase(type);
        if(phaseOrders.length == 1)
            return phaseOrders[0];

        long eachPhaseSecond = getEachPhaseSecond(type, config);
        long elapsedSecond = getSecondDifference(config.startTime(), now);
        int phaseIdx = (int) (elapsedSecond / eachPhaseSecond);

        if(phaseIdx < phaseOrders.length)
            return phaseOrders[ phaseIdx];
        return lastPhase(phaseOrders);
    }

    /** 각각의 스테이지에 부여된 시간을 계산 */
    private long getEachPhaseSecond(InterviewType type, InterviewConfig config) {
        int numberOfPhase = getNumberOfPhase(type);
        long interviewDurationSecond = getSecondDifference(config.startTime(), config.expiredTime());
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

    private long getSecondDifference(LocalDateTime base, LocalDateTime target) {
        long diffSecond = ChronoUnit.SECONDS.between(base, target);
        validMinusSecond(diffSecond);
        return diffSecond;
    }

    private void validMinusSecond(long second) {
        // 비즈니스 로직상 해당 예외가 발생할 수 없음 - DB를 직접 수정할 때 발생할 수 있음.
        if(second <= 0)
            throw new IllegalArgumentException("base가 더 크거나 같다");
    }
}
