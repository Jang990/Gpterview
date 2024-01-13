package com.mock.interview.candidate.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.category.domain.model.ProfileJobCategoryLink;
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
    @Column(name = "candidate_profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String experience;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<ProfileJobCategoryLink> jobLink = new ArrayList<>();

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<ProfileTechLink> techLink = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public static CandidateConfig createProfile(CandidateConfigForm profileDto, Users users, JobCategory department, JobCategory field, List<TechnicalSubjects> techList) {
        CandidateConfig candidateConfig = new CandidateConfig();
        candidateConfig.title = String.format("%s-%s 지원", department.getName(), field.getName());
        candidateConfig.experience = profileDto.getExperience();
        candidateConfig.users = users;

        verifyJobCategory(department, field);
        candidateConfig.jobLink = createJobCategoryLinks(candidateConfig, department, field);

        if(techList != null)
            candidateConfig.techLink = createTechLinks(candidateConfig, techList);

        return candidateConfig;
    }

    private static List<ProfileTechLink> createTechLinks(CandidateConfig candidateConfig, List<TechnicalSubjects> techList) {

        return techList.stream().map((tech) -> ProfileTechLink.createLink(candidateConfig, tech)).toList();
    }

    private static void verifyJobCategory(JobCategory department, JobCategory field) {
        if (department == null || department.isField()
                || field == null || field.isDepartment())
            throw new IllegalArgumentException("분야와 직무는 항상 있어야 함");
    }

    private static List<ProfileJobCategoryLink> createJobCategoryLinks(CandidateConfig candidateConfig, JobCategory department, JobCategory field) {
        return List.of(
                ProfileJobCategoryLink.createLink(candidateConfig, department),
                ProfileJobCategoryLink.createLink(candidateConfig, field)
        );
    }

    public JobCategory getDepartment() {
        if (jobLink.get(0).getJobCategory().isDepartment()) {
            return jobLink.get(0).getJobCategory();
        }
        return jobLink.get(1).getJobCategory();
    }

    public JobCategory getField() {
        if (jobLink.get(0).getJobCategory().isField()) {
            return jobLink.get(0).getJobCategory();
        }
        return jobLink.get(1).getJobCategory();
    }

    public List<TechnicalSubjects> getTechSubjects() {
        return techLink.stream().map(ProfileTechLink::getTechnicalSubjects).toList();
    }
}
