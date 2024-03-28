package com.mock.interview.interview.application;

import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.interview.infra.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewReadOnlyService {
    private final InterviewRepository interviewRepository;
    private final JobCategoryRepository jobCategoryRepository;
}
