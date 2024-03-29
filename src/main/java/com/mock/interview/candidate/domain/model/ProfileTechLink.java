package com.mock.interview.candidate.domain.model;

import com.mock.interview.tech.domain.model.TechnicalSubjects;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileTechLink {
    @Id
    @Column(name = "tech_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateConfig profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_subjects_id")
    private TechnicalSubjects technicalSubjects;

    static ProfileTechLink createLink(CandidateConfig profile, TechnicalSubjects tech) {
        ProfileTechLink link = new ProfileTechLink();
        link.profile = profile;
        link.technicalSubjects = tech;
        return link;
    }
}
