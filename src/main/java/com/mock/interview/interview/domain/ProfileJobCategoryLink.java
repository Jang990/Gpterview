package com.mock.interview.interview.domain;

import com.mock.interview.interview.domain.category.JobCategory;
import com.mock.interview.user.domain.CandidateProfile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileJobCategoryLink {
    @Id
    @Column(name = "job_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateProfile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory jobCategory;

    public static ProfileJobCategoryLink createLink(CandidateProfile profile, JobCategory jobCategory) {
        ProfileJobCategoryLink link = new ProfileJobCategoryLink();
        link.profile = profile;
        link.jobCategory = jobCategory;
        return link;
    }
}
