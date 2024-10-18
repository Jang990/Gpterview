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
        final Interview interview = mock(Interview.class);
        InterviewTimer mockTimer = mock(InterviewTimer.class);
        when(interview.getTimer()).thenReturn(mockTimer);
        when(mockTimer.getExpiredAt()).thenReturn(start);

        assertThrows(
                IsAlreadyTimeoutInterviewException.class,
                () -> tracker.tracePhase(overdueTime(interview), interview)
        );
    }

    private static LocalDateTime overdueTime(Interview interview) {
        return interview.getTimer().getExpiredAt().plusMinutes(1);
    }

    private Interview interview(InterviewType type, int runningMinute) {
        Interview result = mock(Interview.class);
        when(result.getType()).thenReturn(type);

        InterviewTimer mockTimer = mock(InterviewTimer.class);
        when(mockTimer.getStartedAt()).thenReturn(start);
        when(mockTimer.getExpiredAt()).thenReturn(start.plusMinutes(runningMinute));
        when(result.getTimer()).thenReturn(mockTimer);
        return result;
    }

    @Test
    @DisplayName("기술 면접 페이즈 테스트")
    void testTechnicalPhase() {
        final Interview interview = interview(InterviewType.TECHNICAL, runningMinute);

        List<InterviewPhase> traced = traceAllPhase(interview);

        assertAllPhasesIncluded(interview, traced);
        assertPhaseFrequency(interview, traced);
    }

    @Test
    @DisplayName("기술-경험 면접 페이즈 테스트")
    void testTechnicalExperiencePhase() {
        final Interview config = interview(InterviewType.TECHNICAL_EXPERIENCE, runningMinute);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(config, traced);
        assertPhaseFrequency(config, traced);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 페이즈 테스트")
    void testCompositePhase() {
        final Interview interview = interview(InterviewType.COMPOSITE, runningMinute);

        List<InterviewPhase> traced = traceAllPhase(interview);

        assertAllPhasesIncluded(interview, traced);
        assertPhaseFrequency(interview, traced);
    }

    private LocalDateTime startAfter(int minute) {
        return start.plusMinutes(minute);
    }

    private List<InterviewPhase> traceAllPhase(Interview interview) {
        List<InterviewPhase> result = new LinkedList<>();
        for (int elapsed = 0; elapsed < runningMinute; elapsed++) {
            result.add(tracker.tracePhase(startAfter(elapsed), interview));
        }
        return result;
    }

    private void assertPhaseFrequency(Interview interview, List<InterviewPhase> traced) {
        int minimum = runningMinute / phaseLength(interview);
        for (InterviewPhase phase : phaseOrder(interview))
            assertThat(countFrequency(traced, phase)).isGreaterThanOrEqualTo(minimum);
    }

    private int phaseLength(Interview interview) {
        return phaseOrder(interview).length;
    }

    private void assertAllPhasesIncluded(Interview interview, List<InterviewPhase> traced) {
        for (InterviewPhase phase : phaseOrder(interview))
            assertThat(traced.contains(phase)).isTrue();
    }

    private static long countFrequency(List<InterviewPhase> traced, InterviewPhase phase) {
        return traced.stream().filter(p -> p.equals(phase)).count();
    }

    private static InterviewPhase[] phaseOrder(Interview interview) {
        return NOW_InterviewProgressTracer.phaseOrder(interview);
    }

    @Test
    @DisplayName("기술 면접 진행도 테스트")
    void testTechnicalProgress() {
        final Interview interview = interview(InterviewType.TECHNICAL, runningMinute);

        List<List<Double>> phaseProgress = traceAllProgress(interview);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(interview));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("기술-경험 면접 진행도 테스트")
    void testTechnicalExperienceProgress() {
        final Interview interview = interview(InterviewType.TECHNICAL_EXPERIENCE, runningMinute);

        List<List<Double>> phaseProgress = traceAllProgress(interview);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(interview));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 진행도 테스트")
    void testCompositeProgress() {
        final Interview interview = interview(InterviewType.COMPOSITE, runningMinute);

        List<List<Double>> phaseProgress = traceAllProgress(interview);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(interview));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    private List<List<Double>> traceAllProgress(Interview interview) {
        List<List<Double>> result = emptyList(interview);

        int phase = 0;
        double prev = -1;
        for (int elapsed = 0; elapsed < runningMinute; elapsed++) {
            double current = tracker.traceProgress(startAfter(elapsed), interview);
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

    private List<List<Double>> emptyList(Interview interview) {
        List<List<Double>> result = new LinkedList<>();
        for (int i = 0; i < phaseLength(interview); i++) {
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