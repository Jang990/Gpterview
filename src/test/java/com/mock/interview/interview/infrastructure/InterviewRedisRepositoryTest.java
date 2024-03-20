package com.mock.interview.interview.infrastructure;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

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
    @DisplayName("캐시 저장")
    void saveExpiredInterview() {
        InterviewInfo data = mock(InterviewInfo.class);
        long testLong = 1L;
        when(interviewRedisTemplate.opsForValue()).thenReturn(mockOps);
        doNothing().when(mockOps).set(anyString(), eq(data), any());

        interviewRedisRepository.saveInterviewIfActive(testId, data, testLong);

        verify(mockOps, times(1)).set(any(), any(), any());
    }
}