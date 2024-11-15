package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.exception.RequiredExperienceTopicNotFoundException;
import com.mock.interview.interview.domain.exception.RequiredTechTopicNotFoundException;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        LocalDateTime now = LocalDateTime.now();
        Interview interview = TestInterviewBuilder.builder()
                .timer(30, now, now.plusMinutes(30))
                .build();

        LocalDateTime expiredTime = LocalDateTime.now();
        InterviewTimeHolder timeHolder = interviewTimeHolder(expiredTime);

        interview.expire(timeHolder);

        assertThat(interview.getTimer().getExpiredAt()).isEqualTo(expiredTime);
        assertThat(interview.isTimeout(expiredTime)).isTrue();
    }

    @Test
    @DisplayName("만료시간을 시작시간 이전으로 설정 불가능")
    void test5() {
        Interview interview = TestInterviewBuilder.builder().build();
        LocalDateTime expiredTime = interview.getTimer().getStartedAt().minusMinutes(1);
        InterviewTimeHolder timeHolder = interviewTimeHolder(expiredTime);

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
                        interviewTimeHolder(LocalDateTime.now()),
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
                        interviewTimeHolder(LocalDateTime.now()),
                        mock(InterviewTitle.class),
                        mock(InterviewTimer.class),
                        new InterviewConfigForm(type, 30),
                        mock(CandidateInfo.class), emptyTechTopics
                )
        );
    }

}