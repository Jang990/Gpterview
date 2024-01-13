package com.mock.interview.candidate.application;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.domain.model.CandidateProfile;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import com.mock.interview.user.application.UserConvertor;
import com.mock.interview.user.domain.Users;
import com.mock.interview.candidate.domain.exception.CandidateProfileNotFoundException;
import com.mock.interview.candidate.infrastructure.CandidateProfileRepository;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.candidate.presentation.dto.CandidateProfileOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CandidateProfileService {
    private final UserRepository userRepository;
    private final CandidateProfileRepository profileRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    public long create(CandidateProfileForm candidateProfileForm, long userId, List<TechnicalSubjectsResponse> relationalTech) {
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<TechnicalSubjects> techs = technicalSubjectsRepository.findAllById(convertToTechId(relationalTech));
        JobCategory field = jobCategoryRepository.findFieldWithDepartment(candidateProfileForm.getField())
                .orElseThrow(JobCategoryNotFoundException::new);

        CandidateProfile profile = CandidateProfile.createProfile(candidateProfileForm, user, field.getDepartment(), field, techs);
        return profileRepository.save(profile).getId();
    }

    private List<Long> convertToTechId(List<TechnicalSubjectsResponse> relationalTech) {
        return relationalTech.stream().map(TechnicalSubjectsResponse::getId).toList();
    }

    public List<CandidateProfileOverviewDto> findProfiles(long userId) {
        List<CandidateProfile> profiles = profileRepository.findByUserId(userId);
        return UserConvertor.entityToOverviewDto(profiles);
    }

    public CandidateProfileForm findProfile(long profileId, long userId) {
        CandidateProfile profile = profileRepository.findByIdWitUserId(profileId, userId)
                .orElseThrow(CandidateProfileNotFoundException::new);
        return UserConvertor.entityToDto(profile);
    }
}
