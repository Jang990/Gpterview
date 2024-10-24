package com.mock.interview.interview.domain;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewTopicSelectorTest {
    InterviewTopicSelector interviewTopicSelector;
    InterviewTimeHolder timeHolder;
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void beforeEach() {
        timeHolder = mock(InterviewTimeHolder.class);
        when(timeHolder.now()).thenReturn(now);
        interviewTopicSelector = new InterviewTopicSelector(timeHolder);
    }

    @Test
    @DisplayName("인성 면접은 토픽을 지원하지 않으므로 항상 id null 반환")
    void test1() {
        InterviewPhase phase = InterviewPhase.PERSONAL;
        Interview interview = mock(Interview.class);
        when(interview.tracePhase(any())).thenReturn(phase);

        InterviewTopic result = interviewTopicSelector.select(
                interview,
                createMockTechList(emptyList()),
                createMockExperienceList(emptyList())
        );

        assertThat(result.getTopicId()).isNull();
        assertThat(result.getOccurredAt()).isEqualTo(now);
    }

    @ParameterizedTest(name = "면접 진행도: {0}-{1} 토픽 선택")
    @MethodSource("selectingOptions")
    @DisplayName("기술, 경험 페이즈는 진행도에 따라 토픽이 선택됨")
    void test2(
            InterviewPhase phase, double progress,
            List<Long> techIds,
            List<Long> experienceIds,
            long expectedId) {
        Interview interview = createMock(phase, progress);

        InterviewTopic result = interviewTopicSelector.select(
                interview,
                createMockTechList(techIds),
                createMockExperienceList(experienceIds)
        );

        assertThat(result.getTopicId()).isEqualTo(expectedId);
        assertThat(result.getOccurredAt()).isEqualTo(now);
    }

    static Stream<Arguments> selectingOptions() {
        return Stream.of(
                Arguments.arguments(InterviewPhase.TECHNICAL, 0.0, toList(1L, 2L, 3L), emptyList(), 1L),
                Arguments.arguments(InterviewPhase.TECHNICAL, 0.34, toList(1L, 2L, 3L), emptyList(), 2L),
                Arguments.arguments(InterviewPhase.TECHNICAL, 0.67, toList(1L, 2L, 3L), emptyList(), 3L),

                Arguments.arguments(InterviewPhase.EXPERIENCE, 0.0, emptyList(), toList(1L, 2L, 3L), 1L),
                Arguments.arguments(InterviewPhase.EXPERIENCE, 0.34, emptyList(), toList(1L, 2L, 3L), 2L),
                Arguments.arguments(InterviewPhase.EXPERIENCE, 0.67, emptyList(), toList(1L, 2L, 3L), 3L)
        );
    }

    private static List<Long> emptyList() {
        return List.of();
    }

    private static List<Long> toList(long... ids) {
        List<Long> result = new LinkedList<>();
        for (long id : ids) {
            result.add(id);
        }
        return result;
    }

    Interview createMock(InterviewPhase phase, double progress) {
        Interview result = mock(Interview.class);
        when(result.tracePhase(any())).thenReturn(phase);
        when(result.traceProgress(any())).thenReturn(progress);
        return result;
    }

    private List<Experience> createMockExperienceList(List<Long> experienceIds) {
        List<Experience> result = new LinkedList<>();
        for (Long techId : experienceIds) {
            Experience mock = mock(Experience.class);
            when(mock.getId()).thenReturn(techId);
            result.add(mock);
        }
        return result;
    }

    private List<TechnicalSubjects> createMockTechList(List<Long> techIds) {
        List<TechnicalSubjects> result = new LinkedList<>();
        for (Long techId : techIds) {
            TechnicalSubjects mock = mock(TechnicalSubjects.class);
            when(mock.getId()).thenReturn(techId);
            result.add(mock);
        }
        return result;
    }

}