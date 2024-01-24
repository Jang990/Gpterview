package com.mock.interview.creator;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewType;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class InterviewEntityCreator {
    public static Interview create(LocalDateTime expireTime) {
        Interview mock = mock(Interview.class);
        CandidateConfig mockConfig = createConfig();
        when(mock.getCandidateConfig()).thenReturn(mockConfig);
        when(mock.getExpiredTime()).thenReturn(expireTime);
        return mock;
    }

    public static CandidateConfig createConfig() {
        CandidateConfig config = mock(CandidateConfig.class);
        JobCategory field = createField();
        when(config.getAppliedJob()).thenReturn(field);
        when(config.getDepartment()).thenReturn(field.getDepartment());
        when(config.getTechSubjects()).thenReturn(List.of(TechnicalSubjects.create("test 기술")));
        when(config.getExperience()).thenReturn("test 경험");
        when(config.getType()).thenReturn(InterviewType.COMPOSITE);
        return config;
    }

    private static JobCategory createField() {
        JobCategory department = JobCategory.createDepartmentCategory("test 분야");
        return JobCategory.createFieldCategory("test 직무", department);
    }
}
