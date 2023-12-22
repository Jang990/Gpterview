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
    private static final int CONVERSATION_UNIT = 2; // 지원자 - 면접관 : 대화 단위 - Message 2개
    private static final int INTRO_CONVERSATION_COUNT = 1; // 면접관: 면접시작하겠습니다. 면접자: 준비됐습니다.
    private static final int MAX_CONVERSATION_COUNT = 6 + INTRO_CONVERSATION_COUNT; // 대화 수 (지원자-면접자 단위의 대화가 오간 수)
    private static final int MAX_HISTORY_SIZE = MAX_CONVERSATION_COUNT * CONVERSATION_UNIT; // 전체 history 수 제한

    public InterviewProgress getCurrentInterviewProgress(InterviewDetailsDto interviewDetails, MessageHistory history) {
        // TODO: 단계를 분리하는 임시 코드를 적절한 로직으로 수정할 것 - 시간을 고려하도록 바꾸는게 좋을 듯?
        InterviewType interviewType = interviewDetails.getInterviewType();
        if(isFinishedStage(history))
            return new InterviewProgress(InterviewStage.FINISHED, 0);

        return switch (interviewType) {
            case TECHNICAL -> new InterviewProgress(InterviewStage.TECHNICAL, computeProgress(history, MAX_HISTORY_SIZE));
            case EXPERIENCE -> new InterviewProgress(InterviewStage.EXPERIENCE, computeProgress(history, MAX_HISTORY_SIZE));
            case PERSONALITY -> new InterviewProgress(InterviewStage.PERSONAL, computeProgress(history, MAX_HISTORY_SIZE));
            case TECHNICAL_EXPERIENCE -> computeTechnicalAndExperienceInterviewProgress(history);
            case COMPOSITE -> computeCompositeInterviewProgress(history);
        };
    }

    private double computeProgress(MessageHistory history, int stageSize) {
        int historySize = getHistorySize(history);
        historySize %= stageSize;
        return (double) historySize / stageSize * 100;
    }

    private boolean isFinishedStage(MessageHistory history) {
        return getHistorySize(history) >= MAX_HISTORY_SIZE;
    }

    private InterviewProgress computeCompositeInterviewProgress(MessageHistory history) {
        final int stageCnt = 3;
        final int stageSize = MAX_HISTORY_SIZE / stageCnt;
        InterviewStage stage = computeCompositeInterviewStage(history, stageSize);
        return new InterviewProgress(stage, computeProgress(history, stageSize));
    }

    private InterviewStage computeCompositeInterviewStage(MessageHistory history, int stageSize) {
        int historySize = getHistorySize(history);
        if(historySize < stageSize)
            return InterviewStage.TECHNICAL;
        historySize -= stageSize;

        if(historySize < stageSize)
            return InterviewStage.EXPERIENCE;

        if(historySize <= stageSize)
            return InterviewStage.PERSONAL;
        historySize -= stageSize;

        return InterviewStage.FINISHED;
    }

    private InterviewProgress computeTechnicalAndExperienceInterviewProgress(MessageHistory history) {
        final int stageCnt = 2;
        final int stageSize = MAX_HISTORY_SIZE / stageCnt;
        InterviewStage stage = computeTechnicalAndExperienceStage(history, stageSize);
        return new InterviewProgress(stage, computeProgress(history, stageSize));
    }

    private InterviewStage computeTechnicalAndExperienceStage(MessageHistory history, int stageSize) {
        int historySize = getHistorySize(history);
        if(historySize < stageSize)
            return InterviewStage.TECHNICAL;

        if(historySize <= stageSize)
            return InterviewStage.EXPERIENCE;

        return InterviewStage.FINISHED;
    }

    private int getHistorySize(MessageHistory history) {
        return history.getMessages().size() - INTRO_CONVERSATION_COUNT * CONVERSATION_UNIT;
    }

}
