package com.mock.interview.interview;

import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.TestInterviewBuilder;
import com.mock.interview.interview.infra.InterviewRepository;
import com.mock.interview.interview.infra.cache.InterviewCacheRepository;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewUserIds;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.mock.interview.interview.TimeUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewExpirationServiceTest {

    @Mock InterviewTimeHolder interviewTimeHolder;
    @Mock InterviewRepository repository;
    @Mock InterviewCacheRepository interviewCacheRepository;

    @InjectMocks InterviewExpirationService service;

    @Test
    @DisplayName("면접을 만료시키면 시간 홀더의 현재 시간으로 만료시간이 맞춰진다.")
    void test1() {
        Interview myInterview = TestInterviewBuilder.builder()
                .timer(time(1, 0), time(1, 30))
                .build();
        when(interviewTimeHolder.now()).thenReturn(time(1,20));
        when(repository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(myInterview));
        InterviewUserIds interviewUserIds = new InterviewUserIds(1L, 2L);

        service.expire(interviewUserIds);

        assertTrue(myInterview.isTimeout(time(1, 20)));
    }
}