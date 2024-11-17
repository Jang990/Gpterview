package com.mock.interview.interview.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.interview.domain.model.CandidateInfo;
import com.mock.interview.interview.domain.model.CandidateInfoCreator;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateLoader {
    private final UserRepository userRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPositionRepository jobPositionRepository;

    private final CandidateInfoCreator candidateInfoCreator;

    protected CandidateInfo load(long loginId, Long categoryId, Long positionId) {
        Users users = userRepository.findForInterviewSetting(loginId)
                .orElseThrow(UserNotFoundException::new);
        JobCategory category = jobCategoryRepository.findById(categoryId)
                .orElseThrow(JobCategoryNotFoundException::new);
        JobPosition position = jobPositionRepository.findById(positionId)
                .orElseThrow(JobCategoryNotFoundException::new);

        return candidateInfoCreator.create(users, category, position);
    }
}
