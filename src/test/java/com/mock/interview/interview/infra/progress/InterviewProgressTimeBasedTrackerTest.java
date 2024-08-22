package com.mock.interview.interview.infra.progress;

import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InterviewProgressTimeBasedTrackerTest {
    final InterviewProgressTimeBasedTracker tracker = new InterviewProgressTimeBasedTracker();

    final int runningMinute = 30;

    static final LocalDateTime start = LocalDateTime.now();

    @Test
    @DisplayName("기술 면접 페이즈 테스트")
    void testTechnicalPhase() {
        final InterviewType type = InterviewType.TECHNICAL;
        final InterviewConfig config = config(type);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(type, traced);
        assertPhaseFrequency(type, traced);
    }

    @Test
    @DisplayName("기술-경험 면접 페이즈 테스트")
    void testTechnicalExperiencePhase() {
        final InterviewType type = InterviewType.TECHNICAL_EXPERIENCE;
        final InterviewConfig config = config(type);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(type, traced);
        assertPhaseFrequency(type, traced);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 페이즈 테스트")
    void testCompositePhase() {
        final InterviewType type = InterviewType.COMPOSITE;
        final InterviewConfig config = config(type);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(type, traced);
        assertPhaseFrequency(type, traced);
    }

    private InterviewConfig config(InterviewType type) {
        return new InterviewConfig(
                type, start, startAfter(runningMinute)
        );
    }

    private LocalDateTime startAfter(int minute) {
        return start.plusMinutes(minute);
    }

    private List<InterviewPhase> traceAllPhase(InterviewConfig config) {
        List<InterviewPhase> result = new LinkedList<>();
        for (int elapsed = 0; elapsed < runningMinute; elapsed++) {
            result.add(tracker.tracePhase(startAfter(elapsed), config));
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
        return InterviewProgressTimeBasedTracker.phaseOrder(type);
    }

    @Test
    @DisplayName("기술 면접 진행도 테스트")
    void testTechnicalProgress() {
        final InterviewType type = InterviewType.TECHNICAL;
        final InterviewConfig config = config(type);

        List<List<Double>> phaseProgress = traceAllProgress(config);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(type));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("기술-경험 면접 진행도 테스트")
    void testTechnicalExperienceProgress() {
        final InterviewType type = InterviewType.TECHNICAL_EXPERIENCE;
        final InterviewConfig config = config(type);

        List<List<Double>> phaseProgress = traceAllProgress(config);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(type));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 진행도 테스트")
    void testCompositeProgress() {
        final InterviewType type = InterviewType.COMPOSITE;
        final InterviewConfig config = config(type);

        List<List<Double>> phaseProgress = traceAllProgress(config);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(type));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    private List<List<Double>> traceAllProgress(InterviewConfig config) {
        List<List<Double>> result = emptyList(config);

        int phase = 0;
        double prev = -1;
        for (int elapsed = 0; elapsed < runningMinute; elapsed++) {
            double current = tracker.traceProgress(startAfter(elapsed), config);
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

    private List<List<Double>> emptyList(InterviewConfig config) {
        List<List<Double>> result = new LinkedList<>();
        for (int i = 0; i < phaseLength(config.interviewType()); i++) {
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