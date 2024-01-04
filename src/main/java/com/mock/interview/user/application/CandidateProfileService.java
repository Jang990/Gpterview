package com.mock.interview.user.application;

import com.mock.interview.interview.presentation.dto.CandidateProfileDto;
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

    public void create(CandidateProfileDto candidateProfileDto, long userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        CandidateProfile profile = CandidateProfile.createProfile(candidateProfileDto, user);
        profileRepository.save(profile);
    }

    public List<CandidateProfileOverviewDto> findProfiles(long userId) {
        List<CandidateProfile> profiles = profileRepository.findByUserId(userId);
        return UserConvertor.entityToOverviewDto(profiles);
    }

    public CandidateProfileDto findProfile(long profileId, long userId) {
        CandidateProfile profile = profileRepository.findByIdWitUserId(profileId, userId)
                .orElseThrow(CandidateProfileNotFoundException::new);
        return UserConvertor.entityToDto(profile);
    }
}
