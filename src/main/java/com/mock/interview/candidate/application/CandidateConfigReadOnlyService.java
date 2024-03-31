package com.mock.interview.candidate.application;

import com.mock.interview.candidate.domain.exception.CandidateConfigNotFoundException;
import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.candidate.infra.CandidateConfigRepository;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateOverview;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.candidate.domain.model.ProfileTechLink;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateConfigReadOnlyService {
    private final CandidateConfigRepository candidateConfigRepository;
    public List<InterviewCandidateOverview> findOverviews(Long loginId) {
        List<CandidateConfig> userInterview = candidateConfigRepository.findInterviewConfigByUserId(loginId);
        return convertToInterviewOverview(userInterview);
    }

    public InterviewCandidateForm findCandidate(long candidateId, long loginId) {
        CandidateConfig candidateConfig = candidateConfigRepository.findInterviewConfig(candidateId, loginId)
                .orElseThrow(CandidateConfigNotFoundException::new);
        return convert(candidateConfig);
    }

    private List<InterviewCandidateOverview> convertToInterviewOverview(List<CandidateConfig> candidateList) {
        return candidateList.stream()
                .map(
                        candidate -> new InterviewCandidateOverview(
                                candidate.getId(), candidate.getPosition().getName(),
                                candidate.getType(), candidate.getDurationMinutes(),
                                convert(candidate.getTechLink()),
                                candidate.getCreatedAt()
                        )
                ).toList();
    }

    private List<TechnicalSubjectsResponse> convert(List<ProfileTechLink> techLink) {
        return techLink.stream()
                .map(ProfileTechLink::getTechnicalSubjects)
                .map(tech -> new TechnicalSubjectsResponse(tech.getId(), tech.getName())).toList();
    }

    private InterviewCandidateForm convert(CandidateConfig candidateConfig) {
        return new InterviewCandidateForm(
                new CandidateProfileForm(candidateConfig.getPosition().getId(),
                        convertStringList(candidateConfig.getTechLink()),
                        candidateConfig.getExperienceContent().isEmpty() ? null : candidateConfig.getExperienceContent()),
                new InterviewConfigDto(candidateConfig.getType(), candidateConfig.getDurationMinutes())
        );
    }

    private List<String> convertStringList(List<ProfileTechLink> links) {
        // TODO: 나중에 Convert로 통합 필요
        return links.stream()
                .map(ProfileTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getName).toList();
    }
}
