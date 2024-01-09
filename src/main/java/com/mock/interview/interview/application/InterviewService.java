package com.mock.interview.interview.application;

import com.mock.interview.interview.domain.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interview.presentation.dto.CandidateProfileForm;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.domain.exception.CandidateProfileNotFoundException;
import com.mock.interview.user.infrastructure.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository repository;
    private final CandidateProfileRepository profileRepository;

    public long create(long loginId,long candidateProfileId, InterviewDetailsDto interviewSetting) {
        CandidateProfile candidateProfile = profileRepository.findByIdWitUserId(candidateProfileId, loginId)
                .orElseThrow(CandidateProfileNotFoundException::new);
        Interview interview = Interview.startInterview(interviewSetting, candidateProfile.getUsers(), candidateProfile);
        return repository.save(interview).getId();
    }

}
