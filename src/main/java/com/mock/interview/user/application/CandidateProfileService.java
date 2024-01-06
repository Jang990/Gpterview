package com.mock.interview.user.application;

import com.mock.interview.interview.domain.category.JobCategory;
import com.mock.interview.interview.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.interview.infrastructure.JobCategoryRepository;
import com.mock.interview.interview.presentation.dto.CandidateProfileForm;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.domain.Users;
import com.mock.interview.user.domain.exception.CandidateProfileNotFoundException;
import com.mock.interview.user.infrastructure.CandidateProfileRepository;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.user.presentation.dto.CandidateProfileOverviewDto;
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

    public void create(CandidateProfileForm candidateProfileForm, long userId) {
        long tempFieldId = 11; // TODO: candidateProfileForm를 long으로 바꾸고 temp를 변경
        Users user = userRepository.findById(userId).orElseThrow();
        JobCategory field = jobCategoryRepository.findFieldWithDepartment(tempFieldId).orElseThrow(JobCategoryNotFoundException::new);
        CandidateProfile profile = CandidateProfile.createProfile(candidateProfileForm, user, field.getDepartment(), field, null);
        profileRepository.save(profile);
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
