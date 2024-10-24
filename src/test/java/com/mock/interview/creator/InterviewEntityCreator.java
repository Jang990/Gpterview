package com.mock.interview.creator;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.domain.model.InterviewTimer;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class InterviewEntityCreator {
    public static Interview createInterview(LocalDateTime expireTime) {
        Interview mock = mock(Interview.class);
        JobCategory mockCategory = createMockCategory();
        JobPosition mockPosition = createMockPosition();
        when(mock.getCategory()).thenReturn(mockCategory);
        when(mock.getPosition()).thenReturn(mockPosition);

        InterviewTimer mockTimer = mock(InterviewTimer.class);
        when(mockTimer.getExpiredAt()).thenReturn(expireTime);
        when(mock.getTimer()).thenReturn(mockTimer);
        return mock;
    }

    private static JobCategory createMockCategory() {
        JobCategory mockCategory = mock(JobCategory.class);
        when(mockCategory.getId()).thenReturn(1L);
        when(mockCategory.getName()).thenReturn("test 분야");
        return mockCategory;
    }

    private static JobPosition createMockPosition() {
        JobPosition mockPosition = mock(JobPosition.class);
        when(mockPosition.getId()).thenReturn(1L);
        when(mockPosition.getName()).thenReturn("test 분야");
        return mockPosition;
    }
}
