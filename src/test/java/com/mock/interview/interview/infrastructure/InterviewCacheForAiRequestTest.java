package com.mock.interview.interview.infrastructure;

import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InterviewCacheForAiRequestTest {

    @Mock
    InterviewRedisRepository interviewRedisRepository;
    @Mock
    InterviewRepository interviewRepository;

    @InjectMocks
    InterviewCacheForAiRequest cacheRepo;
    long testInterviewId = 25;

    @BeforeEach
    void beforeEach() {
    }

    @Test
    @DisplayName("캐시 히트")
    void test1() {
        InterviewInfo mockInfo = mock(InterviewInfo.class);
        when(interviewRedisRepository.findActiveInterview(testInterviewId)).thenReturn(Optional.of(mockInfo));

        InterviewInfo result = cacheRepo.findAiInterviewSetting(testInterviewId);

        assertThat(result).isEqualTo(mockInfo);
        verify(interviewRepository, times(0)).findInterviewSetting(testInterviewId);
    }

    @Test
    @DisplayName("캐시 미스")
    void test2() {
        LocalDateTime expiredTime = LocalDateTime.now().minusHours(1);
        Interview mockInterview = InterviewEntityCreator.create(expiredTime);

        when(interviewRedisRepository.findActiveInterview(testInterviewId)).thenReturn(Optional.empty());
        when(interviewRepository.findInterviewSetting(testInterviewId)).thenReturn(Optional.of(mockInterview));

        InterviewInfo result = cacheRepo.findAiInterviewSetting(testInterviewId);

        verify(interviewRepository, times(1)).findInterviewSetting(testInterviewId);
        verify(interviewRedisRepository, times(1)).saveInterviewIfActive(testInterviewId, result);

        isEqual(result, mockInterview);
    }

    private void isEqual(InterviewInfo result, Interview mockInterview) {
        assertThat(result.config().end()).isEqualTo(mockInterview.getExpiredTime());
        assertThat(result.config().interviewType()).isEqualTo(mockInterview.getCandidateConfig().getType());
        assertThat(result.profile().department()).isEqualTo(mockInterview.getCandidateConfig().getDepartment().getName());
        assertThat(result.profile().field()).isEqualTo(mockInterview.getCandidateConfig().getAppliedJob().getName());

        Assertions.assertIterableEquals(
                result.profile().experience(),
                List.of(mockInterview.getCandidateConfig().getExperience())
        );

        Assertions.assertIterableEquals(
                result.profile().skills(),
                mockInterview.getCandidateConfig().getTechSubjects().stream().map(TechnicalSubjects::getName).toList()
        );
    }
}