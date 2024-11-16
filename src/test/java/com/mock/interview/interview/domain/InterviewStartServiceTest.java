package com.mock.interview.interview.domain;

import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewStartServiceTest {
    @Mock InterviewTimeHolder timeHolder;
    @Mock ActiveInterviewFinder activeInterviewFinder;
    @InjectMocks InterviewStartService startService;

    @DisplayName("현재 이미 진행중인 면접이 있다면 면접을 진행하지 않는다.")
    @Test
    void test1() {
        when(activeInterviewFinder.hasActiveInterview(any(), any())).thenReturn(true);

        assertThrows(InterviewAlreadyInProgressException.class,
                () -> startService.start(mock(Interview.class), mock(Users.class))
        );
    }

    @DisplayName("현재 이미 진행중인 면접이 있다면 면접 진행한다.")
    @Test
    void test2() {
        when(activeInterviewFinder.hasActiveInterview(any(), any())).thenReturn(false);

        Interview interview = mock(Interview.class);
        startService.start(interview, mock(Users.class));

        // TODO: 행위 검증을 상태검증으로 바꿀 방법
        //      InterviewState 같은 것을 필드로 두고 면접을 진행하면 WAIT_TO_QUESTION이 된다면?
        verify(interview).continueInterview(any());
    }
}