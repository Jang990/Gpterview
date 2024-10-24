package com.mock.interview.interview.domain.model;


import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InterviewTracingTest {

    @Test
    @DisplayName("이미 만료된 면접은 추적 불가능")
    void testBoundary2() {
        LocalDateTime start = LocalDateTime.now();
        Interview interview = createInterview(InterviewType.TECHNICAL, start, 1);
        interview.expire(timeHolder(start));

        assertThrows(IsAlreadyTimeoutInterviewException.class, () -> interview.traceProgress(start));
    }

    @ParameterizedTest(name = "{0} {2}분 중 {1}분 경과, 결과: {3}페이즈 {4}% 진행")
    @MethodSource("tracingProgressOptions")
    @DisplayName("모든 진행도는 모든 페이즈에서 균등하게 등장")
    void traceProgress(
            InterviewType type, int elapsed, int duration,
            InterviewPhase expectedPhase, double expectedProgress) {
        LocalDateTime current = LocalDateTime.now();
        Interview interview = createInterview(type, current, duration);

        InterviewProgress result = interview.traceProgress(elapsedTime(current, elapsed));

        assertThat(result.getPhase()).isEqualTo(expectedPhase);
        assertThat(result.getProgressOfPhase().progress()).isEqualTo(expectedProgress);
    }

    static Stream<Arguments> tracingProgressOptions() {
        return Stream.of(
                // 단일 페이즈 면접
                Arguments.arguments(InterviewType.TECHNICAL, 0, 4, InterviewPhase.TECHNICAL, 0.00),
                Arguments.arguments(InterviewType.TECHNICAL, 1, 4, InterviewPhase.TECHNICAL, 0.25),
                Arguments.arguments(InterviewType.TECHNICAL, 2, 4, InterviewPhase.TECHNICAL, 0.50),
                Arguments.arguments(InterviewType.TECHNICAL, 3, 4, InterviewPhase.TECHNICAL, 0.75),

                Arguments.arguments(InterviewType.EXPERIENCE, 0, 2, InterviewPhase.EXPERIENCE, 0.0),
                Arguments.arguments(InterviewType.EXPERIENCE, 1, 2, InterviewPhase.EXPERIENCE, 0.5),

                Arguments.arguments(InterviewType.PERSONALITY, 0, 2, InterviewPhase.PERSONAL, 0.0),
                Arguments.arguments(InterviewType.PERSONALITY, 1, 2, InterviewPhase.PERSONAL, 0.5),

                // 2단계 페이즈 면접
                Arguments.arguments(InterviewType.TECHNICAL_EXPERIENCE, 0, 4, InterviewPhase.TECHNICAL, 0.0),
                Arguments.arguments(InterviewType.TECHNICAL_EXPERIENCE, 1, 4, InterviewPhase.TECHNICAL, 0.5),
                Arguments.arguments(InterviewType.TECHNICAL_EXPERIENCE, 2, 4, InterviewPhase.EXPERIENCE, 0.0),
                Arguments.arguments(InterviewType.TECHNICAL_EXPERIENCE, 3, 4, InterviewPhase.EXPERIENCE, 0.5),

                // 3단계 페이즈 면접
                Arguments.arguments(InterviewType.COMPOSITE, 0, 6, InterviewPhase.TECHNICAL, 0.0),
                Arguments.arguments(InterviewType.COMPOSITE, 1, 6, InterviewPhase.TECHNICAL, 0.5),
                Arguments.arguments(InterviewType.COMPOSITE, 2, 6, InterviewPhase.EXPERIENCE, 0.0),
                Arguments.arguments(InterviewType.COMPOSITE, 3, 6, InterviewPhase.EXPERIENCE, 0.5),
                Arguments.arguments(InterviewType.COMPOSITE, 4, 6, InterviewPhase.PERSONAL, 0.0),
                Arguments.arguments(InterviewType.COMPOSITE, 5, 6, InterviewPhase.PERSONAL, 0.5)
        );
    }

    private Interview createInterview(InterviewType type, LocalDateTime start, int duration) {
        InterviewTimeHolder timeHolder = timeHolder(start);
        JobCategory category = JobCategory.createCategory("MyCategory");
        JobPosition position = JobPosition.create("MyBackendPosition", category);

        return Interview.create(
                timeHolder,
                new InterviewConfigForm(type, duration),
                mock(Users.class), category, position
        );
    }

    private static InterviewTimeHolder timeHolder(LocalDateTime now) {
        InterviewTimeHolder timeHolder = mock(InterviewTimeHolder.class);
        when(timeHolder.now()).thenReturn(now);
        return timeHolder;
    }

    private LocalDateTime elapsedTime(LocalDateTime current, int elapsed) {
        return current.plusMinutes(elapsed);
    }
}