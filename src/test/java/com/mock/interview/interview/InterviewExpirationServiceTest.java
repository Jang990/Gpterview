package com.mock.interview.interview;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.cache.dto.InterviewConfig;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.model.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewExpirationServiceTest {
    @Mock InterviewTimeHolder interviewTimeHolder;
    @Mock InterviewRepository repository;
    @Mock InterviewCacheRepository interviewCacheRepository;

    @InjectMocks InterviewExpirationService service;

    private Interview myInterview;
    private LocalDateTime interviewCreationTime;

    @BeforeEach
    void setUp() {
        interviewCreationTime = LocalDateTime.now();
        when(interviewTimeHolder.now()).thenReturn(interviewCreationTime);

        int durationMinutes = 30;
        Users mockUser = mock(Users.class);
        JobCategory category = JobCategory.createCategory("abc");
        JobPosition position = JobPosition.create("abc", category);
        myInterview = Interview.create(
                interviewTimeHolder,
                new InterviewConfigForm(InterviewType.PERSONALITY, durationMinutes),
                mockUser, category, position
        );
    }

    @Test
    @DisplayName("면접을 만료시킴")
    void test1() {
        when(repository.findByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.of(myInterview));
        LocalDateTime expiredTime = LocalDateTime.now();
        when(interviewTimeHolder.now()).thenReturn(expiredTime);
        InterviewUserIds interviewUserIds = new InterviewUserIds(1L, 2L);

        service.expire(interviewUserIds);

        assertThat(myInterview.isTimeout(expiredTime)).isTrue();
    }
}