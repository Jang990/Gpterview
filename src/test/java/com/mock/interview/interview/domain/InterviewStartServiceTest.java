package com.mock.interview.interview.domain;

import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.domain.exception.InterviewAlreadyInProgressException;
import com.mock.interview.interview.domain.model.CandidateInfo;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.InterviewTimer;
import com.mock.interview.interview.infra.InterviewRepository;
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
    @Mock InterviewRepository repository;
    @Mock InterviewCreator interviewCreator;
    @InjectMocks InterviewStartService startService;

    @DisplayName("현재 이미 진행중인 면접이 있다면, 면접을 진행하지 않는다.")
    @Test
    void test1() {
        when(activeInterviewFinder.hasActiveInterview(any(), any())).thenReturn(true);

        assertThrows(InterviewAlreadyInProgressException.class,
                () -> startService.start(
                        mock(CandidateInfo.class),
                        mock(InterviewTopicDto.class),
                        mock(InterviewTimer.class)
                )
        );
    }

    @DisplayName("이미 진행중인 면접이 없다면, 면접Creator가 생성한 면접을 저장하고 면접을 진행한 후 반환한다.")
    @Test
    void test2() {
        Interview interview = mock(Interview.class);
        when(interviewCreator.create(any(), any(), any())).thenReturn(interview);
        when(activeInterviewFinder.hasActiveInterview(any(), any())).thenReturn(false);

        Interview result = startService.start(
                mock(CandidateInfo.class),
                mock(InterviewTopicDto.class),
                mock(InterviewTimer.class)
        );

        assertEquals(interview, result);

        // TODO: 행위 검증을 상태검증으로 바꿀 방법
        //      InterviewState 같은 것을 필드로 두고 면접을 진행하면 WAIT_TO_QUESTION이 된다면?
        //      InterviewState가 있고 InterviewState에 따라 도메인 이벤트 호출이 불가능한 상태가 된다면
        //      굳이 도메인 레벨에서 repository로 save를 호출하지 않아도 된다.
        verify(interview).continueInterview(any());
        verify(repository).save(any());
    }
}