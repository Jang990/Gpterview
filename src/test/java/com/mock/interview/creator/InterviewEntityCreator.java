package com.mock.interview.creator;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.tech.domain.model.TechnicalSubjects;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class InterviewEntityCreator {
    public static Interview createCategory(LocalDateTime expireTime) {
        Interview mock = mock(Interview.class);
        CandidateConfig mockConfig = createConfig();
        when(mock.getExpiredTime()).thenReturn(expireTime);
        return mock;
    }

    public static CandidateConfig createConfig() {
        CandidateConfig config = mock(CandidateConfig.class);
        JobCategory category = createCategory();
        JobPosition position = createPosition(category);
        when(config.getCategory()).thenReturn(category);
        when(config.getPosition()).thenReturn(position);
        when(config.getTechSubjects()).thenReturn(List.of(TechnicalSubjects.create("test 기술")));
        when(config.getType()).thenReturn(InterviewType.COMPOSITE);
        return config;
    }

    private static JobCategory createCategory() {
        return JobCategory.createCategory("test 분야");
    }

    private static JobPosition createPosition(JobCategory category) {
        return JobPosition.create("test 직무", category);
    }
}
