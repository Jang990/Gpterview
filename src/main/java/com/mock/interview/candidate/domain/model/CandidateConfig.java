package com.mock.interview.candidate.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.presentation.dto.InterviewDetailsDto;
import com.mock.interview.interview.presentation.dto.InterviewSettingDto;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.candidate.presentation.dto.CandidateConfigForm;
import com.mock.interview.user.domain.Users;
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

    private String experience;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobCategory appliedJob;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<ProfileTechLink> techLink = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public static CandidateConfig createProfile(
            InterviewSettingDto interviewSettingDto,
            Users users, JobCategory field, List<TechnicalSubjects> techList) {
        CandidateConfigForm profileDto = interviewSettingDto.getProfile();
        InterviewDetailsDto interviewDetails = interviewSettingDto.getInterviewDetails();

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
            CandidateConfig candidateConfig, CandidateConfigForm profileDto,
            List<TechnicalSubjects> techList, JobCategory field
    ) {
        candidateConfig.experience = profileDto.getExperience();
        if(techList != null)
            candidateConfig.techLink = createTechLinks(candidateConfig, techList);

        setCategory(candidateConfig, field);
    }

    private static void configInterviewSetting(CandidateConfig candidateConfig, InterviewDetailsDto interviewDetailsDto) {
        candidateConfig.type = interviewDetailsDto.getInterviewType();
        candidateConfig.durationMinutes = interviewDetailsDto.getDurationMinutes();
    }

    private static List<ProfileTechLink> createTechLinks(CandidateConfig candidateConfig, List<TechnicalSubjects> techList) {
        return techList.stream().map((tech) -> ProfileTechLink.createLink(candidateConfig, tech)).toList();
    }

    private static void verifyJobCategory(JobCategory field) {
        if (field == null || field.isDepartment())
            throw new IllegalArgumentException("직무는 항상 있어야 함");
    }

    public JobCategory getDepartment() {
        return getAppliedJob().getDepartment();
    }

    public List<TechnicalSubjects> getTechSubjects() {
        return techLink.stream().map(ProfileTechLink::getTechnicalSubjects).toList();
    }
}
