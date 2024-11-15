package com.mock.interview.interview.domain.model;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewTimerCreatorTest {

    @Mock private InterviewTimeHolder timeHolder;
    @InjectMocks private InterviewTimerCreator creator;

    @DisplayName("면접 타이머는 0분 이하로 설정할 수 없다.")
    @Test
    void test1() {
        assertThrows(IllegalArgumentException.class, () ->
                creator.create(0)
        );
    }

    @DisplayName("면접 타이머는 1시간을 초과해서 설정할 수 없다.")
    @Test
    void test2() {
        assertThrows(IllegalArgumentException.class, () ->
                creator.create(61)
        );
    }

    @DisplayName("면접 타이머는 타임 홀더에서 제공하는 시간과 기간 파라미터를 기준으로 생성된다.")
    @Test
    void test3() {
        LocalDateTime now = LocalDateTime.now();
        when(timeHolder.now()).thenReturn(now);

        InterviewTimer result = creator.create(30);

        assertEquals(30, result.getDurationMinutes());
        assertEquals(now, result.getStartedAt());
        assertEquals(now.plusMinutes(30), result.getExpiredAt());
    }

}