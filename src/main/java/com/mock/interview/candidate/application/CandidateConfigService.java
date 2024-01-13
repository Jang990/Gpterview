package com.mock.interview.candidate.application;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;
import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import com.mock.interview.user.application.UserConvertor;
import com.mock.interview.user.domain.Users;
import com.mock.interview.candidate.domain.exception.CandidateConfigNotFoundException;
import com.mock.interview.candidate.infrastructure.CandidateConfigRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.candidate.presentation.dto.CandidateConfigOverviewDto;
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

    public long create(CandidateConfigForm candidateConfigForm, long userId, List<TechnicalSubjectsResponse> relationalTech) {
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<TechnicalSubjects> techs = technicalSubjectsRepository.findAllById(convertToTechId(relationalTech));
        JobCategory field = jobCategoryRepository.findFieldWithDepartment(candidateConfigForm.getField())
                .orElseThrow(JobCategoryNotFoundException::new);

        CandidateConfig profile = CandidateConfig.createProfile(candidateConfigForm, user, field.getDepartment(), field, techs);
        return profileRepository.save(profile).getId();
    }

    private List<Long> convertToTechId(List<TechnicalSubjectsResponse> relationalTech) {
        return relationalTech.stream().map(TechnicalSubjectsResponse::getId).toList();
    }

    public List<CandidateConfigOverviewDto> findProfiles(long userId) {
        List<CandidateConfig> profiles = profileRepository.findByUserId(userId);
        return UserConvertor.entityToOverviewDto(profiles);
    }

    public CandidateConfigForm findProfile(long profileId, long userId) {
        CandidateConfig profile = profileRepository.findByIdWitUserId(profileId, userId)
                .orElseThrow(CandidateConfigNotFoundException::new);
        return UserConvertor.entityToDto(profile);
    }
}
