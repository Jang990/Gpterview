package com.mock.interview.interview.application;

import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.interview.domain.Interview;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interview.presentation.dto.InterviewCandidateOverview;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewReadOnlyService {
    private final InterviewRepository interviewRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public List<InterviewCandidateOverview> findOverviews(Long loginId) {
        List<Interview> userInterview = interviewRepository.findUserInterviewWithProfileAndTech(loginId);
        return convertToInterviewOverview(userInterview);
    }

    public InterviewSettingDto findInterviewConfig(long interviewId, long loginId) {
        Interview interview = interviewRepository.findInterviewSetting(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        List<JobCategory> interviewCategory = jobCategoryRepository.findInterviewCategory(interview.getCandidateConfig().getId());
        return convert(interview, interviewCategory);
    }

    private InterviewSettingDto convert(Interview interview, List<JobCategory> interviewCategory) {
        CandidateConfigForm profileForm = new CandidateConfigForm(
                null,
                convert(interview.getCandidateConfig().getTechLink()).stream().map(TechnicalSubjectsResponse::getName).toList(),
                interview.getCandidateConfig().getExperience()
        );
        if(interviewCategory.get(0).isField())
            profileForm.setField(interviewCategory.get(0).getId());
        else
            profileForm.setField(interviewCategory.get(1).getId());

        return new InterviewSettingDto(
                profileForm,
                new InterviewDetailsDto(interview.getType(), interview.getDurationMinutes())
        );
    }

    private List<InterviewCandidateOverview> convertToInterviewOverview(List<Interview> userInterview) {
        return userInterview.stream()
                .map(
                        interview -> new InterviewCandidateOverview(
                                interview.getId(), interview.getTitle(),
                                interview.getType(), interview.getDurationMinutes(),
                                convert(interview.getCandidateConfig().getTechLink()),
                                interview.getCreatedAt()
                        )
                ).toList();
    }

    private List<TechnicalSubjectsResponse> convert(List<ProfileTechLink> techLink) {
        return techLink.stream()
                .map(ProfileTechLink::getTechnicalSubjects)
                .map(tech -> new TechnicalSubjectsResponse(tech.getId(), tech.getName())).toList();
    }
}
