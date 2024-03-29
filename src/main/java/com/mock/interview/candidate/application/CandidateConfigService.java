package com.mock.interview.candidate.application;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.candidate.infra.CandidateConfigRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CandidateConfigService {
    private final UserRepository userRepository;
    private final CandidateConfigRepository profileRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    public long create(InterviewCandidateForm interviewCandidateForm, long userId, List<Long> relationalTechIds) {
        CandidateProfileForm candidateProfileForm = interviewCandidateForm.getProfile();
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<TechnicalSubjects> techs = technicalSubjectsRepository.findAllById(relationalTechIds);
        JobCategory field = jobCategoryRepository.findFieldWithDepartment(candidateProfileForm.getField())
                .orElseThrow(JobCategoryNotFoundException::new);

        CandidateConfig profile = CandidateConfig.createProfile(interviewCandidateForm, user, field, techs);
        return profileRepository.save(profile).getId();
    }
}
