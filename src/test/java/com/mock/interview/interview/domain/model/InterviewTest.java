package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
        JobCategory category = JobCategory.createCategory("MyCategory");
        JobPosition wrongPosition = JobPosition.create(
                "MyPosition", JobCategory.createCategory("NonRelatedCategory"));
        InterviewTimeHolder timeHolder = interviewTimeHolder(LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () ->
                Interview.create(
                        timeHolder, mock(InterviewTitle.class),
                        new InterviewConfigForm(mock(InterviewType.class), 1),
                        mock(Users.class), category, wrongPosition
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

}