package com.mock.interview.interview;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.TestInterviewBuilder;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
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

@ExtendWith(MockitoExtension.class)
class InterviewExpirationServiceTest {
    @Mock InterviewTimeHolder interviewTimeHolder;
    @Mock InterviewRepository repository;
    @Mock InterviewCacheRepository interviewCacheRepository;

    @InjectMocks InterviewExpirationService service;

    private LocalDateTime interviewCreationTime;

    @BeforeEach
    void setUp() {
        interviewCreationTime = LocalDateTime.now();
        when(interviewTimeHolder.now()).thenReturn(interviewCreationTime);
    }

    @Test
    @DisplayName("면접을 만료시킴")
    void test1() {
        Interview myInterview = TestInterviewBuilder.builder().build();
        LocalDateTime expiredTime = TestInterviewBuilder.DEFAULT_START_AT;
        InterviewUserIds interviewUserIds = new InterviewUserIds(1L, 2L);
        when(repository.findByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.of(myInterview));
        when(interviewTimeHolder.now()).thenReturn(expiredTime);

        service.expire(interviewUserIds);

        assertThat(myInterview.isTimeout(expiredTime)).isTrue();
    }
}