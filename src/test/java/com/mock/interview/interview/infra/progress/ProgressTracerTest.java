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
import static org.junit.jupiter.api.Assertions.*;

class ProgressTracerTest {
    final ProgressTracer tracker = new ProgressTracer();

    final int runningMinute = 30;

    static final LocalDateTime start = LocalDateTime.now();

    @Test
    @DisplayName("경계값 - 0분 테스트")
    void testBoundary1() {
        final InterviewConfig config = zeroDurationComposite();
        assertThrows(
                IllegalArgumentException.class,
                () -> tracker.tracePhase(start, config)
        );

    }

    @Test
    @DisplayName("경계값 - 만료시간 이후")
    void testBoundary2() {
        final InterviewConfig config = compositeConfig();
        assertThrows(
                IllegalArgumentException.class,
                () -> tracker.tracePhase(overdueTime(config), config)
        );
    }

    private static LocalDateTime overdueTime(InterviewConfig config) {
        return config.expiredTime().plusMinutes(1);
    }

    private static InterviewPhase firstPhase(InterviewConfig config) {
        return phaseOrder(config)[0];
    }

    private InterviewConfig zeroDurationComposite() {
        return new InterviewConfig(InterviewType.COMPOSITE, start, start);
    }

    @Test
    @DisplayName("기술 면접 페이즈 테스트")
    void testTechnicalPhase() {
        final InterviewConfig config = config(InterviewType.TECHNICAL);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(config, traced);
        assertPhaseFrequency(config, traced);
    }

    @Test
    @DisplayName("기술-경험 면접 페이즈 테스트")
    void testTechnicalExperiencePhase() {
        final InterviewConfig config = config(InterviewType.TECHNICAL_EXPERIENCE);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(config, traced);
        assertPhaseFrequency(config, traced);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 페이즈 테스트")
    void testCompositePhase() {
        final InterviewConfig config = config(InterviewType.COMPOSITE);

        List<InterviewPhase> traced = traceAllPhase(config);

        assertAllPhasesIncluded(config, traced);
        assertPhaseFrequency(config, traced);
    }

    private InterviewConfig compositeConfig() {
        return new InterviewConfig(InterviewType.COMPOSITE, start, startAfter(runningMinute));
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

    private void assertPhaseFrequency(InterviewConfig config, List<InterviewPhase> traced) {
        int minimum = runningMinute / phaseLength(config);
        for (InterviewPhase phase : phaseOrder(config))
            assertThat(countFrequency(traced, phase)).isGreaterThanOrEqualTo(minimum);
    }

    private int phaseLength(InterviewConfig config) {
        return phaseOrder(config).length;
    }

    private void assertAllPhasesIncluded(InterviewConfig config, List<InterviewPhase> traced) {
        for (InterviewPhase phase : phaseOrder(config))
            assertThat(traced.contains(phase)).isTrue();
    }

    private static long countFrequency(List<InterviewPhase> traced, InterviewPhase phase) {
        return traced.stream().filter(p -> p.equals(phase)).count();
    }

    private static InterviewPhase[] phaseOrder(InterviewConfig config) {
        return ProgressTracer.phaseOrder(config);
    }

    @Test
    @DisplayName("기술 면접 진행도 테스트")
    void testTechnicalProgress() {
        final InterviewConfig config = config(InterviewType.TECHNICAL);

        List<List<Double>> phaseProgress = traceAllProgress(config);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(config));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("기술-경험 면접 진행도 테스트")
    void testTechnicalExperienceProgress() {
        final InterviewConfig config = config(InterviewType.TECHNICAL_EXPERIENCE);

        List<List<Double>> phaseProgress = traceAllProgress(config);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(config));
        assertProgressIsIncreasing(phaseProgress);
        assertEndDifferenceLessThan(0.1, phaseProgress);
    }

    @Test
    @DisplayName("복합{기술-경험-인성} 면접 진행도 테스트")
    void testCompositeProgress() {
        final InterviewConfig config = config(InterviewType.COMPOSITE);

        List<List<Double>> phaseProgress = traceAllProgress(config);

        assertThat(phaseProgress.size()).isEqualTo(phaseLength(config));
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
        for (int i = 0; i < phaseLength(config); i++) {
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