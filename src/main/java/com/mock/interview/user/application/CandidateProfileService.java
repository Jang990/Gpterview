package com.mock.interview.user.application;

import com.mock.interview.interview.presentation.dto.CandidateProfileDto;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.domain.Users;
import com.mock.interview.user.infrastructure.CandidateProfileRepository;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateProfileService {
    private final UserRepository userRepository;
    private final CandidateProfileRepository profileRepository;

    public void create(CandidateProfileDto candidateProfileDto, long userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        CandidateProfile profile = CandidateProfile.createProfile(candidateProfileDto, user);
        profileRepository.save(profile);
    }
}
