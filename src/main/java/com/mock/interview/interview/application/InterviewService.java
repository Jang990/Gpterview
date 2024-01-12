package com.mock.interview.interview.application;

import com.mock.interview.interview.InterviewDomain;
import com.mock.interview.interview.domain.Interview;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.interview.domain.category.TechnicalSubjects;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.infrastructure.InterviewConversationRepository;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.category.infrastructure.JobCategoryRepository;
import com.mock.interview.interview.infrastructure.interview.dto.InterviewConfig;
import com.mock.interview.interview.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.interview.infrastructure.interview.dto.InterviewProfile;
import com.mock.interview.interview.presentation.dto.CandidateProfileForm;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.domain.exception.CandidateProfileNotFoundException;
import com.mock.interview.user.infrastructure.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository repository;
    private final CandidateProfileRepository profileRepository;
    private final InterviewConversationRepository conversationRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final InterviewDomain interviewDomain;

    public long create(long loginId,long candidateProfileId, InterviewDetailsDto interviewSetting) {
        CandidateProfile candidateProfile = profileRepository.findByIdWitUserId(candidateProfileId, loginId)
                .orElseThrow(CandidateProfileNotFoundException::new);
        Interview interview = Interview.startInterview(interviewSetting, candidateProfile.getUsers(), candidateProfile);
        return repository.save(interview).getId();
    }

    @Transactional(readOnly = true)
    public InterviewInfo findInterviewForAIRequest(long loginId, long interviewId) {
        Interview interview = repository.findInterviewSetting(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);

        List<JobCategory> category = jobCategoryRepository.findInterviewCategory(interview.getCandidateProfile().getId());
        if (category.get(0).isDepartment()) {
            return convert(interview, category.get(0), category.get(1));
        } else {
            return convert(interview, category.get(1), category.get(0));
        }
    }

    private static InterviewInfo convert(Interview interview, JobCategory department, JobCategory field) {
        CandidateProfile profile = interview.getCandidateProfile();
        return new InterviewInfo(
                new InterviewProfile(
                        department.getName(), field.getName(),
                        profile.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                        List.of(profile.getExperience())
                ),
                new InterviewConfig(interview.getType(), interview.getDurationMinutes())
        );
    }

    public void startInterview(CandidateProfileForm profile, InterviewDetailsDto interviewDetails) {
        // TODO: Redis - 인터뷰 횟수 검증
        // TODO: Redis - 인터뷰 정보 캐싱(인터뷰 정보, 진행시간 등등)
        // TODO: DB - 인터뷰 정보 영구 저장
    }

    public void proceedInterview() {
        // TODO: Redis - 캐싱된 인터뷰 시작 정보 불러오기
        // TODO: AI - 시작 정보 기반 요청 : Response 반환
        // TODO: DB - Response 저장
        // return MSG
    }
}
