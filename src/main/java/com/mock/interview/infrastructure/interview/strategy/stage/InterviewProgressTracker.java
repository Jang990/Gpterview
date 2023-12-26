package com.mock.interview.infrastructure.interview.strategy.stage;

import com.mock.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.presentation.dto.InterviewType;
import org.springframework.stereotype.Component;

/**
 * 현재 인터뷰 진행 정도를 파악.
 */
@Component
public class InterviewProgressTracker {

    // TODO: 상수말고 시간으로 설정하는 것을 고려해 볼 것
    private final int CONVERSATION_UNIT = 2; // 지원자 - 면접관 : 대화 단위 - Message 2개
    private final int INTRO_CONVERSATION_COUNT = 1; // 면접관: 면접시작하겠습니다. 면접자: 준비됐습니다.
    private final int MAX_CONVERSATION_COUNT = 24; // 대화 수 (지원자-면접자 단위의 대화가 오간 수)
    private final int MAX_CONVERSATION_SIZE = MAX_CONVERSATION_COUNT * CONVERSATION_UNIT; // 전체 history 수 제한

    private final InterviewStage[] COMPOSITE_STAGE_ORDER = {InterviewStage.TECHNICAL, InterviewStage.EXPERIENCE, InterviewStage.PERSONAL};
    private final InterviewStage[] TECH_EX_STAGE_ORDER = {InterviewStage.TECHNICAL, InterviewStage.EXPERIENCE};

    public InterviewProgress getCurrentInterviewProgress(InterviewDetailsDto interviewDetails, MessageHistory history) {
        // TODO: 단계를 분리하는 임시 코드를 적절한 로직으로 수정할 것 - 시간을 고려하도록 바꾸는게 좋을 듯?
        InterviewType interviewType = interviewDetails.getInterviewType();
        if(isFinishedStage(history))
            return new InterviewProgress(InterviewStage.FINISHED, 0);

        return switch (interviewType) {
            case TECHNICAL -> new InterviewProgress(InterviewStage.TECHNICAL, computeStageProgress(history, MAX_CONVERSATION_SIZE));
            case EXPERIENCE -> new InterviewProgress(InterviewStage.EXPERIENCE, computeStageProgress(history, MAX_CONVERSATION_SIZE));
            case PERSONALITY -> new InterviewProgress(InterviewStage.PERSONAL, computeStageProgress(history, MAX_CONVERSATION_SIZE));
            case TECHNICAL_EXPERIENCE -> getTechnicalAndExperienceProgress(history);
            case COMPOSITE -> getCompositeProgress(history);
        };
    }

    private boolean isFinishedStage(MessageHistory history) {
        return history.getMessages().size() >= MAX_CONVERSATION_SIZE + INTRO_CONVERSATION_COUNT * CONVERSATION_UNIT;
    }

    /**
     * Stage 진행도 백분률 계산
     */
    private double computeStageProgress(MessageHistory history, int eachStageSize) {
        int historySize = getHistorySizeWithoutIntro(history);
        historySize %= eachStageSize;
        return (double) historySize / eachStageSize;
    }

    private InterviewProgress getCompositeProgress(MessageHistory history) {
        final int numberOfStages = COMPOSITE_STAGE_ORDER.length;
        final int eachStageSize = MAX_CONVERSATION_SIZE / numberOfStages;
        InterviewStage stage = computeStage(COMPOSITE_STAGE_ORDER, history, eachStageSize);
        return new InterviewProgress(stage, computeStageProgress(history, eachStageSize));
    }

    private InterviewProgress getTechnicalAndExperienceProgress(MessageHistory history) {
        final int numberOfStages = TECH_EX_STAGE_ORDER.length;
        final int eachStageSize = MAX_CONVERSATION_SIZE / numberOfStages;
        InterviewStage stage = computeStage(TECH_EX_STAGE_ORDER, history, eachStageSize);
        return new InterviewProgress(stage, computeStageProgress(history, eachStageSize));
    }

    private InterviewStage computeStage(InterviewStage[] stageOrder, MessageHistory history, int eachStageSize) {
        int historySize = getHistorySizeWithoutIntro(history);
        int stageLevel = historySize / eachStageSize;
        return (stageLevel < stageOrder.length) ? stageOrder[stageLevel] : InterviewStage.FINISHED;
    }

    private int getHistorySizeWithoutIntro(MessageHistory history) {
        return history.getMessages().size() - INTRO_CONVERSATION_COUNT * CONVERSATION_UNIT;
    }
}
