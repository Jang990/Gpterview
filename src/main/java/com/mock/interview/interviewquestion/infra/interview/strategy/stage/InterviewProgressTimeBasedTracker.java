package com.mock.interview.interviewquestion.infra.interview.strategy.stage;

import com.mock.interview.candidate.presentation.dto.InterviewType;
import com.mock.interview.interviewquestion.infra.interview.dto.InterviewConfig;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/** 진행 시간을 기반으로 현재 면접 진행 정도를 파악. */
@Component
@Getter
public class InterviewProgressTimeBasedTracker {

    public final InterviewStage[] COMPOSITE_STAGE_ORDER = {InterviewStage.TECHNICAL, InterviewStage.EXPERIENCE, InterviewStage.PERSONAL};
    private final InterviewStage[] TECH_EX_STAGE_ORDER = {InterviewStage.TECHNICAL, InterviewStage.EXPERIENCE};
    private final InterviewStage[] TECH_STAGE_ORDER = new InterviewStage[]{InterviewStage.TECHNICAL};
    private final InterviewStage[] EXPERIENCE_STAGE_ORDER = new InterviewStage[]{InterviewStage.EXPERIENCE};
    private final InterviewStage[] PERSONAL_STAGE_ORDER = new InterviewStage[]{InterviewStage.PERSONAL};

    public InterviewStage[] getStageOrder(InterviewType type) {
        return getStages(type).clone();
    }

    public InterviewProgress getCurrentInterviewProgress(InterviewConfig config) {
        InterviewType interviewType = config.interviewType();
        LocalDateTime now = LocalDateTime.now();

        InterviewStage interviewStage = computeStage(now, interviewType, config);
        double progress = computeStageProgress(now, interviewType, config);

        return new InterviewProgress(interviewStage, progress);
    }

    /** 해당 면접 타입에 몇 개의 스테이지가 있는지 */
    private int getNumberOfStage(InterviewType interviewType) {
        return switch (interviewType) {
            case TECHNICAL, PERSONALITY, EXPERIENCE -> 1;
            case TECHNICAL_EXPERIENCE -> 2;
            case COMPOSITE -> 3;
        };
    }

    /** Stage 진행도 백분률 계산 */
    private double computeStageProgress(LocalDateTime now, InterviewType type, InterviewConfig config) {
        long eachStageSecond = getEachStageSecond(type, config);
        long elapsedSecond = getSecondDifference(config.startTime(), now);
        long aa = elapsedSecond % eachStageSecond;
        return (double) aa / eachStageSecond;
    }

    /** 현재 어떤 스테이지를 진행중인지 계산 */
    private InterviewStage computeStage(LocalDateTime now, InterviewType type, InterviewConfig config) {
        InterviewStage[] stageOrders = getStages(type);
        if(stageOrders.length == 1)
            return stageOrders[0];

        long eachStageSecond = getEachStageSecond(type, config);
        long elapsedSecond = getSecondDifference(config.startTime(), now);
        int stageIdx = (int) (elapsedSecond / eachStageSecond);

        if(stageIdx < stageOrders.length)
            return stageOrders[ stageIdx];
        return lastStage(stageOrders);
    }

    /** 각각의 스테이지에 부여된 시간을 계산 */
    private long getEachStageSecond(InterviewType type, InterviewConfig config) {
        int numberOfStage = getNumberOfStage(type);
        long interviewDurationSecond = getSecondDifference(config.startTime(), config.expiredTime());
        return interviewDurationSecond / numberOfStage;
    }

    private InterviewStage[] getStages(InterviewType type) {
        return switch (type) {
            case TECHNICAL -> TECH_STAGE_ORDER;
            case PERSONALITY -> PERSONAL_STAGE_ORDER;
            case EXPERIENCE -> EXPERIENCE_STAGE_ORDER;
            case TECHNICAL_EXPERIENCE -> TECH_EX_STAGE_ORDER;
            case COMPOSITE -> COMPOSITE_STAGE_ORDER;
        };
    }

    private InterviewStage lastStage(InterviewStage[] stageOrders) {
        return stageOrders[stageOrders.length - 1];
    }

    private long getSecondDifference(LocalDateTime base, LocalDateTime target) {
        long result = ChronoUnit.SECONDS.between(base, target); // base가 더 작아야 +
        validMinusSecond(result);
        return result;
    }

    private void validMinusSecond(long second) {
        if(second < 0)
            throw new IllegalArgumentException();
    }
}
