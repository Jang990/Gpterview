package com.mock.interview.candidate.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.category.domain.model.ProfileJobCategoryLink;
import com.mock.interview.tech.domain.model.ProfileTechLink;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.candidate.presentation.dto.CandidateProfileForm;
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
public class CandidateProfile extends BaseTimeEntity {
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

    // TODO: 임시로 techList는 null로 전부 적용해놓음
    public static CandidateProfile createProfile(CandidateProfileForm profileDto, Users users, JobCategory department, JobCategory field, List<TechnicalSubjects> techList) {
        CandidateProfile candidateProfile = new CandidateProfile();
        candidateProfile.title = "새 프로필";
        candidateProfile.experience = profileDto.getExperience();
        candidateProfile.users = users;

        verifyJobCategory(department, field);
        candidateProfile.jobLink = createJobCategoryLinks(candidateProfile, department, field);

        if(techList != null)
            candidateProfile.techLink = createTechLinks(candidateProfile, techList);

        return candidateProfile;
    }

    private static List<ProfileTechLink> createTechLinks(CandidateProfile candidateProfile, List<TechnicalSubjects> techList) {

        return techList.stream().map((tech) -> ProfileTechLink.createLink(candidateProfile, tech)).toList();
    }

    private static void verifyJobCategory(JobCategory department, JobCategory field) {
        if (department == null || department.isField()
                || field == null || field.isDepartment())
            throw new IllegalArgumentException("분야와 직무는 항상 있어야 함");
    }

    private static List<ProfileJobCategoryLink> createJobCategoryLinks(CandidateProfile candidateProfile, JobCategory department, JobCategory field) {
        return List.of(
                ProfileJobCategoryLink.createLink(candidateProfile, department),
                ProfileJobCategoryLink.createLink(candidateProfile, field)
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
