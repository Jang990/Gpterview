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
    private static final int CONVERSATION_UNIT = 2; // 지원자 - 면접자 : 대화 단위 - Message 2개
    private static final int MAX_CONVERSATION_COUNT = 15; // 대화 수 (지원자-면접자 단위의 대화가 오간 수)
    private static final int MAX_HISTORY_SIZE = MAX_CONVERSATION_COUNT * CONVERSATION_UNIT; // 전체 history 수 제한

    public InterviewStage getCurrentInterviewStage(InterviewDetailsDto interviewDetails, MessageHistory history) {
        // TODO: 단계를 분리하는 임시 코드를 적절한 로직으로 수정할 것 - 시간을 고려하도록 바꾸는게 좋을 듯?
        InterviewType interviewType = interviewDetails.getInterviewType();
        if(isFinishedStage(history))
            return InterviewStage.FINISHED;

        return switch (interviewType) {
            case TECHNICAL -> InterviewStage.TECHNICAL;
            case EXPERIENCE -> InterviewStage.EXPERIENCE;
            case PERSONALITY -> InterviewStage.PERSONAL;
            case TECHNICAL_EXPERIENCE -> computeTechnicalAndExperienceStage(history);
            case COMPOSITE -> computeCompositeStage(history);
        };
    }

    private boolean isFinishedStage(MessageHistory history) {
        return getHistorySize(history) > MAX_HISTORY_SIZE;
    }

    private InterviewStage computeCompositeStage(MessageHistory history) {
        final int stageCnt = 3;
        final int stageSize = MAX_HISTORY_SIZE / stageCnt;
        int historySize = getHistorySize(history);
        if(historySize < stageSize)
            return InterviewStage.TECHNICAL;
        historySize -= stageSize;

        if(historySize < stageSize)
            return InterviewStage.EXPERIENCE;

        return InterviewStage.PERSONAL;
    }

    private InterviewStage computeTechnicalAndExperienceStage(MessageHistory history) {
        final int stageCnt = 2;
        final int stageSize = MAX_HISTORY_SIZE / stageCnt;
        int historySize = getHistorySize(history);
        System.out.println(stageSize);
        if(historySize < stageSize)
            return InterviewStage.TECHNICAL;

        return InterviewStage.EXPERIENCE;
    }

    private int getHistorySize(MessageHistory history) {
        return history.getMessages().size();
    }

}
