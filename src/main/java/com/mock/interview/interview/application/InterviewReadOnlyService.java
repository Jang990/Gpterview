package com.mock.interview.interview.application;

import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.interview.domain.Interview;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interview.presentation.dto.InterviewCandidateOverview;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewReadOnlyService {
    private final InterviewRepository interviewRepository;
    private final JobCategoryRepository jobCategoryRepository;
}
