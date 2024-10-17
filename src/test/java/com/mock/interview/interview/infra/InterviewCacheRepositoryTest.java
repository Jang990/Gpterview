package com.mock.interview.interview.infra;

import com.mock.interview.interview.domain.model.InterviewTechLink;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.InterviewRedisRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.creator.InterviewEntityCreator;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InterviewCacheRepositoryTest {

    @Mock
    InterviewRedisRepository interviewRedisRepository;
    @Mock
    InterviewRepository interviewRepository;

    @InjectMocks
    InterviewCacheRepository cacheRepo;
    long testInterviewId = 25;

    @BeforeEach
    void beforeEach() {
    }

    @Test
    @DisplayName("캐시 히트")
    void test1() {
        InterviewInfo mockInfo = mock(InterviewInfo.class);
        when(interviewRedisRepository.find(testInterviewId)).thenReturn(Optional.of(mockInfo));

        InterviewInfo result = cacheRepo.findProgressingInterviewInfo(testInterviewId);

        assertThat(result).isEqualTo(mockInfo);
        verify(interviewRepository, times(0)).findInterviewSetting(testInterviewId);
    }

    @Test
    @DisplayName("캐시 미스 - 진행중인 인터뷰 저장")
    void test2() {
        LocalDateTime activeTime = LocalDateTime.now().plusHours(1);
        Interview mockInterview = InterviewEntityCreator.createInterview(activeTime);

        when(interviewRedisRepository.find(testInterviewId)).thenReturn(Optional.empty());
        when(interviewRepository.findInterviewSetting(testInterviewId)).thenReturn(Optional.of(mockInterview));
        when(mockInterview.getExpiredAt()).thenReturn(activeTime);

        InterviewInfo result = cacheRepo.findProgressingInterviewInfo(testInterviewId);

        verify(interviewRepository, times(1)).findInterviewSetting(testInterviewId);
        verify(interviewRedisRepository, times(1)).save(eq(testInterviewId), eq(result), anyLong());

        isEqual(result, mockInterview);
    }

    @Test
    @DisplayName("캐시 미스 - 만료된 인터뷰")
    void test3() {
        LocalDateTime expiredTime = LocalDateTime.now().minusHours(1);
        Interview mockInterview = InterviewEntityCreator.createInterview(expiredTime);

        when(interviewRedisRepository.find(testInterviewId)).thenReturn(Optional.empty());
        when(interviewRepository.findInterviewSetting(testInterviewId)).thenReturn(Optional.of(mockInterview));
        when(mockInterview.getExpiredAt()).thenReturn(expiredTime);

        InterviewInfo result = cacheRepo.findProgressingInterviewInfo(testInterviewId);

        verify(interviewRepository, times(1)).findInterviewSetting(testInterviewId);
        verify(interviewRedisRepository, times(0)).save(eq(testInterviewId), eq(result), anyLong());

        isEqual(result, mockInterview);
    }

    private void isEqual(InterviewInfo result, Interview mockInterview) {
        assertThat(result.config().expiredTime()).isEqualTo(mockInterview.getExpiredAt());
        assertThat(result.config().type()).isEqualTo(mockInterview.getType());
        assertThat(result.profile().category().getName()).isEqualTo(mockInterview.getCategory().getName());
        assertThat(result.profile().field().getName()).isEqualTo(mockInterview.getPosition().getName());

        // TODO: 경험 테스트 추가할 것
        /*Assertions.assertIterableEquals(
                result.profile().experience(),
                mockInterview.getExperienceLink().get()
        );*/

        Assertions.assertIterableEquals(
                result.profile().skills(),
                mockInterview.getTechLink().stream()
                        .map(InterviewTechLink::getTechnicalSubjects)
                        .map(TechnicalSubjects::getName).toList()
        );
    }
}