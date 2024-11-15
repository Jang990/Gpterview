package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.presentation.dto.InterviewResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActiveInterviewServiceTest {
    @Mock InterviewRepository interviewRepository;
    @InjectMocks ActiveInterviewService activeInterviewService;

    @Test
    @DisplayName("사용자의 활성화된 면접의 제목과 ID를 반환")
    void test1() {
        long userId = 1L;
        long interviewId = 222L;
        String interviewTitle = "My-Interview";
        Interview myMockInterview = createMock(interviewId, interviewTitle);
        when(interviewRepository.findActiveInterview(anyLong()))
                .thenReturn(Optional.of(myMockInterview));

        Optional<InterviewResponse> result = activeInterviewService.find(userId);

        assertThat(result.isPresent()).isTrue();
        InterviewResponse response = result.get();
        assertThat(response.getInterviewId()).isEqualTo(interviewId);
        assertThat(response.getTitle()).isEqualTo(interviewTitle);
    }

    @Test
    @DisplayName("활성화된 면접이 없다면 Empty")
    void test2() {
        long userId = 1L;
        when(interviewRepository.findActiveInterview(anyLong()))
                .thenReturn(Optional.empty());

        Optional<InterviewResponse> result = activeInterviewService.find(userId);

        assertThat(result.isEmpty()).isTrue();
    }

    private Interview createMock(long interviewId, String interviewTitle) {
        Interview result = mock(Interview.class);
        when(result.getId()).thenReturn(interviewId);
        when(result.getTitle()).thenReturn(interviewTitle);
        return result;
    }
}