package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InterviewTest {

    @Test
    @DisplayName("면접 시간이 0분 이하인 면접 생성 불가능")
    void test1() {
        assertThrows(IllegalArgumentException.class, () ->
                TestInterviewBuilder.builder().durationMinute(0).build()
        );
    }

    private static InterviewTimeHolder interviewTimeHolder(LocalDateTime now) {
        InterviewTimeHolder timeHolder = mock(InterviewTimeHolder.class);
        when(timeHolder.now()).thenReturn(now);
        return timeHolder;
    }

    @Test
    @DisplayName("카테고리와 관련없는 포지션으로 면접 생성 불가능")
    void test3() {
        InterviewTimeHolder timeHolder = interviewTimeHolder(LocalDateTime.now());
        JobCategory nonRelatedCategory = mock(JobCategory.class);
        JobCategory relatedCategory = mock(JobCategory.class);
        JobPosition position = mock(JobPosition.class);
        when(position.getCategory()).thenReturn(relatedCategory);
        InterviewTopicDto topics = InterviewTopicDto.builder()
                .position(position)
                .category(nonRelatedCategory)
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                Interview.create(
                        timeHolder, mock(InterviewTitle.class),
                        new InterviewConfigForm(mock(InterviewType.class), 1),
                        mock(Users.class), topics
                )
        );
    }

    @Test
    @DisplayName("만료시 expiredAt이 타임홀더에 맞춰짐")
    void test4() {
        Interview interview = TestInterviewBuilder.builder().build();
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

        assertThrows(IllegalArgumentException.class,
                () -> Interview.create(
                        interviewTimeHolder(LocalDateTime.now()),
                        mock(InterviewTitle.class),
                        new InterviewConfigForm(type, 30),
                        mock(Users.class), emptyTechTopics
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

        assertThrows(IllegalArgumentException.class,
                () -> Interview.create(
                        interviewTimeHolder(LocalDateTime.now()),
                        mock(InterviewTitle.class),
                        new InterviewConfigForm(type, 30),
                        mock(Users.class), emptyTechTopics
                )
        );
    }

}