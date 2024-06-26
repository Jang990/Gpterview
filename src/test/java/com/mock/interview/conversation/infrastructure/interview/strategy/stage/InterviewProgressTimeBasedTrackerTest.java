package com.mock.interview.conversation.infrastructure.interview.strategy.stage;

import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.progress.InterviewProgressTimeBasedTracker;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class InterviewProgressTimeBasedTrackerTest {
    InterviewProgressTimeBasedTracker tracker = new InterviewProgressTimeBasedTracker();

    @Test
    @DisplayName("복합-3단계, 60분 진행")
    void test1() {
        InterviewType testType = InterviewType.COMPOSITE;
        long interviewDurationMinute = 60;
        test(testType, interviewDurationMinute);
    }

    @Test
    @DisplayName("테크-1단계 - 30분 진행")
    void test2() {
        InterviewType testType = InterviewType.TECHNICAL;
        long interviewDurationMinute = 30;
        test(testType, interviewDurationMinute);
    }

    @Test
    @DisplayName("{테크-경험}-2단계 - 50분 진행")
    void test3() {
        InterviewType testType = InterviewType.TECHNICAL_EXPERIENCE;
        long interviewDurationMinute = 50;
        test(testType, interviewDurationMinute);
    }

    private void test(InterviewType testType, long interviewDurationMinute) {
        InterviewPhase[] stageOrder = tracker.getPhaseOrder(testType);
        long eachStageMinute = interviewDurationMinute / stageOrder.length;
        for (int elapsedMinute = 0; elapsedMinute <= interviewDurationMinute; elapsedMinute++) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.minusMinutes(elapsedMinute);
            LocalDateTime expiredTime = startTime.plusMinutes(interviewDurationMinute);
            InterviewConfig active1HoursConfig = new InterviewConfig(testType, startTime, expiredTime);

            InterviewPhase phase = tracker.tracePhase(now, active1HoursConfig);
            double progress = tracker.traceProgress(now, active1HoursConfig);

            long stageElapsedMinute = (elapsedMinute % eachStageMinute);
            assertThat((double) stageElapsedMinute / eachStageMinute).isEqualTo(progress);
            int stageIdx = (int) (elapsedMinute / eachStageMinute);
            if(stageIdx < stageOrder.length)
                assertThat(stageOrder[stageIdx]).isEqualTo(phase);
            else
                assertThat(stageOrder[stageOrder.length - 1]).isEqualTo(phase);
        }
    }

}