package com.mock.interview.interview.domain;

import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.InterviewTimer;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.presentation.dto.InterviewType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NOW_InterviewProgressTracerTest {
    final NOW_InterviewProgressTracer tracker = new NOW_InterviewProgressTracer();

    final int runningMinute = 30;

    static final LocalDateTime start = LocalDateTime.now();

    @Test
    @DisplayName("이미 만료된 면접은 추적 불가능")
    void testBoundary2() {
        InterviewTimer mockTimer = mock(InterviewTimer.class);
        when(mockTimer.getExpiredAt()).thenReturn(start);

        assertThrows(
                IsAlreadyTimeoutInterviewException.class,
                () -> tracker.tracePhase(overdueTime(start), mockTimer, mock(InterviewType.class))
        );
    }

    private static LocalDateTime overdueTime(LocalDateTime base) {
        return base.plusMinutes(1);
    }

    private InterviewTimer mockTimer(int runningMinute) {
        InterviewTimer mockTimer = mock(InterviewTimer.class);
        when(mockTimer.getStartedAt()).thenReturn(start);
        when(mockTimer.getExpiredAt()).thenReturn(start.plusMinutes(runningMinute));
        return mockTimer;
    }

    @Test
    @DisplayName("기술 면접 페이즈 테스트")
    void testTechnicalPhase() {
        InterviewType type = InterviewType.TECHNICAL;
        InterviewTimer timer = mockTimer(runningMinute);

        List<InterviewPhase> traced = traceAllPhase(timer, type);

        assertAllPhasesIncluded(type, traced);
        assertPhaseFrequency(type, traced);
    }

    @Test
    @DisplayName("기술-경험 면접 페이즈 테스트")
    void testTechnicalExperiencePhase() {
        InterviewType type = InterviewType.TECHNICAL_EXPERIENCE;
        InterviewTimer timer = mockTimer(runningMinute);

        List<InterviewPhase> traced = traceAllPhase(timer, type);

        assertAllPhasesIncluded(type, traced);
        assertPhaseFrequency(type, traced);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 페이즈 테스트")
    void testCompositePhase() {
        InterviewType type = InterviewType.COMPOSITE;
        InterviewTimer timer = mockTimer(runningMinute);

        List<InterviewPhase> traced = traceAllPhase(timer, type);

        assertAllPhasesIncluded(type, traced);
        assertPhaseFrequency(type, traced);
    }

    private LocalDateTime startAfter(int minute) {
        return start.plusMinutes(minute);
    }

    private List<InterviewPhase> traceAllPhase(InterviewTimer timer, InterviewType type) {
        List<InterviewPhase> result = new LinkedList<>();
        for (int elapsed = 0; elapsed < runningMinute; elapsed++) {
            result.add(tracker.tracePhase(startAfter(elapsed), timer, type));
        }
        return result;
    }

    private void assertPhaseFrequency(InterviewType type, List<InterviewPhase> traced) {
        int minimum = runningMinute / phaseLength(type);
        for (InterviewPhase phase : phaseOrder(type))
            assertThat(countFrequency(traced, phase)).isGreaterThanOrEqualTo(minimum);
    }

    private int phaseLength(InterviewType type) {
        return phaseOrder(type).length;
    }

    private void assertAllPhasesIncluded(InterviewType type, List<InterviewPhase> traced) {
        for (InterviewPhase phase : phaseOrder(type))
            assertThat(traced.contains(phase)).isTrue();
    }

    private static long countFrequency(List<InterviewPhase> traced, InterviewPhase phase) {
        return traced.stream().filter(p -> p.equals(phase)).count();
    }

    private static InterviewPhase[] phaseOrder(InterviewType type) {
        return NOW_InterviewProgressTracer.phaseOrder(type);
    }

    @Test
    @DisplayName("기술 면접 진행도 테스트")
    void testTechnicalProgress() {
        InterviewTimer timer = mockTimer(runningMinute);
        InterviewType type = InterviewType.TECHNICAL;
        List<List<Double>> phaseProgress = traceAllProgress(timer, type);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(type));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("기술-경험 면접 진행도 테스트")
    void testTechnicalExperienceProgress() {
        InterviewType type = InterviewType.TECHNICAL_EXPERIENCE;
        InterviewTimer timer = mockTimer(runningMinute);

        List<List<Double>> phaseProgress = traceAllProgress(timer, type);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(type));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 진행도 테스트")
    void testCompositeProgress() {
        InterviewType type = InterviewType.COMPOSITE;
        InterviewTimer timer = mockTimer(runningMinute);

        List<List<Double>> phaseProgress = traceAllProgress(timer, type);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(type));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    private List<List<Double>> traceAllProgress(InterviewTimer timer, InterviewType type) {
        List<List<Double>> result = emptyList(type);

        int phase = 0;
        double prev = -1;
        for (int elapsed = 0; elapsed < runningMinute; elapsed++) {
            double current = tracker.traceProgress(startAfter(elapsed), timer, type);
            if (isPhaseChanged(prev, current))
                phase++;
            prev = current;
            result.get(phase).add(current);
        }
        return result;
    }


    private void assertEndDifferenceLessThan(final double diff, List<List<Double>> phaseProgress) {
        if(phaseProgress.size() <= 1)
            return;

        double min = 1.0, max = 0.0;
        for (List<Double> phase : phaseProgress) {
            min = Math.min(min, phase.get(phase.size() - 1));
            max = Math.max(max, phase.get(phase.size() - 1));
        }

        assertThat(max - min).isLessThan(diff);
    }

    private void assertProgressIsIncreasing(List<List<Double>> phaseProgress) {
        for (List<Double> progress : phaseProgress) {
            for (int i = 1; i < progress.size(); i++) {
                assertThat(progress.get(i)).isGreaterThanOrEqualTo(progress.get(i - 1));
            }
        }
    }

    private List<List<Double>> emptyList(InterviewType type) {
        List<List<Double>> result = new LinkedList<>();
        for (int i = 0; i < phaseLength(type); i++) {
            addEmptyList(result);
        }
        return result;
    }

    private boolean isPhaseChanged(double prev, double current) {
        return prev >= current;
    }

    private void addEmptyList(List<List<Double>> result) {
        result.add(new LinkedList<>());
    }
}