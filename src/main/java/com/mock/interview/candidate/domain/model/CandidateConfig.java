package com.mock.interview.candidate.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.candidate.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
import com.mock.interview.user.domain.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidateConfig extends BaseTimeEntity {
    @Id
    @Column(name = "candidate_config_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType type; // TODO: DB에 넣어야
    @Column(nullable = false)
    private int durationMinutes;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<Experience> experience = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private JobCategory appliedJob;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<ProfileTechLink> techLink = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public static CandidateConfig createProfile(
            InterviewCandidateForm interviewCandidateForm,
            Users users, JobCategory field, List<TechnicalSubjects> techList) {
        CandidateProfileForm profileDto = interviewCandidateForm.getProfile();
        InterviewConfigDto interviewDetails = interviewCandidateForm.getInterviewDetails();

        CandidateConfig candidateConfig = new CandidateConfig();
        candidateConfig.users = users;
        configInterviewSetting(candidateConfig, interviewDetails);
        setCandidateProfile(candidateConfig, profileDto, techList, field);

        return candidateConfig;
    }

    private static void setCategory(CandidateConfig candidateConfig, JobCategory field) {
        verifyJobCategory(field);
        candidateConfig.appliedJob = field;
    }

    private static void setCandidateProfile(
            CandidateConfig candidateConfig, CandidateProfileForm profileDto,
            List<TechnicalSubjects> techList, JobCategory field
    ) {
        for (String experience : profileDto.getExperiences()) {
            candidateConfig.createExperience(experience);
        }

        if(techList != null)
            candidateConfig.techLink = createTechLinks(candidateConfig, techList);

        setCategory(candidateConfig, field);
    }

    public Experience createExperience(String experience) {
        // TODO: 만약 isDeleted 필드가 생긴다면 체크해줘야함.
        Experience createdExperience = Experience.create(this, experience);
        this.experience.add(createdExperience);
        return createdExperience;
    }

    private static void configInterviewSetting(CandidateConfig candidateConfig, InterviewConfigDto interviewConfigDto) {
        candidateConfig.type = interviewConfigDto.getInterviewType();
        candidateConfig.durationMinutes = interviewConfigDto.getDurationMinutes();
    }

    private static List<ProfileTechLink> createTechLinks(CandidateConfig candidateConfig, List<TechnicalSubjects> techList) {
        return techList.stream().map((tech) -> ProfileTechLink.createLink(candidateConfig, tech)).toList();
    }

    private static void verifyJobCategory(JobCategory field) {
        if (field == null || field.isDepartment())
            throw new IllegalArgumentException("직무는 항상 있어야 함");
    }

    public List<String> getExperienceContent() {
        return experience.stream()
                .map(Experience::getExperience).toList();
    }

    public JobCategory getDepartment() {
        return getAppliedJob().getDepartment();
    }

    public List<TechnicalSubjects> getTechSubjects() {
        return getTechLink().stream().map(ProfileTechLink::getTechnicalSubjects).toList();
    }
}
