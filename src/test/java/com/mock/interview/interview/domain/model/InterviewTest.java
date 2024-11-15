package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.RequiredExperienceTopicNotFoundException;
import com.mock.interview.interview.domain.exception.RequiredTechTopicNotFoundException;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.mock.interview.interview.TimeUtils.time;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InterviewTest {
    private static InterviewTimeHolder interviewTimeHolder(LocalDateTime now) {
        InterviewTimeHolder timeHolder = mock(InterviewTimeHolder.class);
        when(timeHolder.now()).thenReturn(now);
        return timeHolder;
    }

    @Test
    @DisplayName("만료시 expiredAt이 타임홀더에 맞춰짐")
    void test4() {
        Interview interview = TestInterviewBuilder.builder()
                .timer(time(1, 0), time(1, 30))
                .build();
        InterviewTimeHolder timeHolder = interviewTimeHolder(time(1, 10));

        interview.expire(timeHolder);

        assertThat(time(1,10)).isEqualTo(interview.getTimer().getExpiredAt());
        assertThat(interview.isTimeout(time(1,10))).isTrue();
    }

    @Test
    @DisplayName("만료시간을 시작시간 이전으로 설정 불가능")
    void test5() {
        Interview interview = TestInterviewBuilder.builder()
                .timer(time(2, 0), time(2, 30))
                .build();
        InterviewTimeHolder timeHolder = interviewTimeHolder(time(1, 0));

        assertThrows(IllegalArgumentException.class, () -> interview.expire(timeHolder));
    }

    @Test
    @DisplayName("InterviewType이 기술 주제가 필요하다면 TechTopics가 없다면 예외가 발생한다.")
    void test6() {
        InterviewType type = mock(InterviewType.class);
        when(type.requiredTechTopics()).thenReturn(true);
        InterviewTopicDto emptyTechTopics = InterviewTopicDto.builder()
                .techTopics(Collections.EMPTY_LIST)
                .build();

        assertThrows(RequiredTechTopicNotFoundException.class,
                () -> Interview.create(
                        mock(InterviewTitle.class),
                        mock(InterviewTimer.class),
                        new InterviewConfigForm(type, 30),
                        mock(CandidateInfo.class), emptyTechTopics
                )
        );
    }

    @Test
    @DisplayName("InterviewType이 경험 주제가 필요한데 ExperienceTopics가 없다면 예외가 발생한다.")
    void test7() {
        InterviewType type = mock(InterviewType.class);
        when(type.requiredExperienceTopics()).thenReturn(true);
        InterviewTopicDto emptyTechTopics = InterviewTopicDto.builder()
                .experienceTopics(Collections.EMPTY_LIST)
                .build();

        assertThrows(RequiredExperienceTopicNotFoundException.class,
                () -> Interview.create(
                        mock(InterviewTitle.class),
                        mock(InterviewTimer.class),
                        new InterviewConfigForm(type, 30),
                        mock(CandidateInfo.class), emptyTechTopics
                )
        );
    }

    @Test
    @DisplayName("면접을 만료시키면 시간 홀더의 현재 시간으로 만료시간이 맞춰진다.")
    void test8() {
        InterviewTimeHolder timeHolder = interviewTimeHolder(time(1, 20));
        Interview myInterview = TestInterviewBuilder.builder()
                .timer(time(1, 0), time(1, 30))
                .build();

        myInterview.expire(timeHolder);

        assertEquals(time(1, 20), myInterview.getTimer().getExpiredAt());
    }

    @Test
    @DisplayName("시간에 따라 면접 타임아웃 여부를 파악할 수 있다.")
    void test9() {
        Interview myInterview = TestInterviewBuilder.builder()
                .timer(time(1, 0), time(1, 30))
                .build();

        assertTrue(myInterview.isTimeout(time(1, 30)));
        assertTrue(myInterview.isTimeout(time(1, 40)));
        assertFalse(myInterview.isTimeout(time(1, 20)));
    }

}