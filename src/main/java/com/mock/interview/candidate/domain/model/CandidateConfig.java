package com.mock.interview.candidate.domain.model;

import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewCandidateForm;
import com.mock.interview.candidate.presentation.dto.InterviewType;
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
import java.util.Comparator;
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
    private InterviewType type;
    @Column(nullable = false)
    private int durationMinutes;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<Experience> experience = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id")
    private JobPosition position;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<ProfileTechLink> techLink = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public static CandidateConfig createProfile(
            InterviewConfigDto interviewConfigDto, Users users,
            JobCategory category, JobPosition position
    ) {
        CandidateConfig candidateConfig = new CandidateConfig();
        candidateConfig.users = users;
        candidateConfig.type = interviewConfigDto.getInterviewType();
        candidateConfig.durationMinutes = interviewConfigDto.getDurationMinutes();
        linkJob(candidateConfig, category, position);

        return candidateConfig;
    }

    public void linkTech(List<TechnicalSubjects> techList) {
        if(techList == null)
            throw new IllegalArgumentException();

        this.techLink = techList.stream()
                .map((tech) -> ProfileTechLink.createLink(this, tech)).toList();
    }

    public void setExperience(List<String> experiences) {
        for (String experience : experiences) {
            Experience createdExperience = Experience.create(this, experience);
            this.experience.add(createdExperience);
        }
    }

    public static void linkJob(CandidateConfig config,JobCategory category, JobPosition position) {
        verifyJob(category, position);
        config.category = category;
        config.position = position;
    }

    private static void verifyJob(JobCategory category, JobPosition position) {
        if (category == null || position == null)
            throw new IllegalArgumentException("직무는 항상 있어야 함");
        if(position == null || position.getCategory().getId().equals(category.getId()))
            throw new IllegalArgumentException("position이 category과 관계 없음");
    }

    public List<String> getExperienceContent() {
        return experience.stream()
                .sorted(Comparator.comparing(Experience::getId))
                .map(Experience::getExperience).toList();
    }

    public List<TechnicalSubjects> getTechSubjects() {
        return getTechLink().stream().map(ProfileTechLink::getTechnicalSubjects).toList();
    }
}
