package com.mock.interview.interview.infrastructure;

import com.mock.interview.conversation.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterviewRedisRepositoryTest {

    @Mock
    RedisTemplate<String, InterviewInfo> interviewRedisTemplate;

    @InjectMocks
    InterviewRedisRepository interviewRedisRepository;

    ValueOperations<String, InterviewInfo> mockOps;
    long testId = 1L;

    @BeforeEach
    void beforeEach() {
        mockOps = mock(ValueOperations.class);
    }

    @Test
    @DisplayName("캐싱 데이터 가져오기")
    void findActiveInterview() {
        when(interviewRedisTemplate.opsForValue()).thenReturn(mockOps);
        InterviewInfo mock = mock(InterviewInfo.class);
        when(mockOps.get(any())).thenReturn(mock);

        Optional<InterviewInfo> result = interviewRedisRepository.findActiveInterview(testId);

        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get()).isEqualTo(mock);
    }

    @Test
    @DisplayName("만료된 인터뷰 - 캐싱 안함")
    void saveExpiredInterview() {
        InterviewInfo data = mock(InterviewInfo.class);
        InterviewConfig config = mock(InterviewConfig.class);
        when(data.config()).thenReturn(config);
        when(config.end()).thenReturn(LocalDateTime.now().minusHours(1));

        interviewRedisRepository.saveInterviewIfActive(testId, data);

        verify(mockOps, times(0)).set(any(), any());
    }

    @Test
    @DisplayName("진행중 인터뷰 - 캐싱")
    void saveActiveInterview() {
        when(interviewRedisTemplate.opsForValue()).thenReturn(mockOps);

        InterviewInfo data = mock(InterviewInfo.class);
        InterviewConfig config = mock(InterviewConfig.class);
        when(data.config()).thenReturn(config);
        when(config.end()).thenReturn(LocalDateTime.now().plusHours(1));

        interviewRedisRepository.saveInterviewIfActive(testId, data);

        verify(mockOps, times(1)).set(any(), any(), any());
    }
}