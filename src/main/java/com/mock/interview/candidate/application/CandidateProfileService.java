package com.mock.interview.candidate.application;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.tech.infrastructure.TechnicalSubjectsRepository;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.domain.model.CandidateProfile;
import com.mock.interview.user.application.UserConvertor;
import com.mock.interview.user.domain.Users;
import com.mock.interview.candidate.domain.exception.CandidateProfileNotFoundException;
import com.mock.interview.candidate.infrastructure.CandidateProfileRepository;
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

    public long create(CandidateProfileForm candidateProfileForm, long userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        List<TechnicalSubjects> savedTech = saveTech(candidateProfileForm.getSkills());
        JobCategory field = jobCategoryRepository.findFieldWithDepartment(candidateProfileForm.getField())
                .orElseThrow(JobCategoryNotFoundException::new);
        CandidateProfile profile = CandidateProfile.createProfile(candidateProfileForm, user, field.getDepartment(), field, savedTech); // TODO: techList 조회 및 초기화 필요.
        return profileRepository.save(profile).getId();
    }

    private List<TechnicalSubjects> saveTech(List<String> skills) {
        List<TechnicalSubjects> techList = technicalSubjectsRepository.findTech(skills);

        List<String> savedNames = techList.stream().map(TechnicalSubjects::getName).toList();
        skills.stream()
                .filter(notSavedSkill -> !savedNames.contains(notSavedSkill))
                .map(TechnicalSubjects::create)
                .map(technicalSubjectsRepository::save)
                .forEach(techList::add);

        return techList;
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
