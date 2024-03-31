package com.mock.interview.candidate.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Experience extends BaseTimeEntity {
    @Id
    @Column(name = "candidate_experience_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1500)
    private String experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateConfig profile;

    static Experience create(CandidateConfig profile, String content) {
        Experience experience = new Experience();
        experience.profile = profile;
        experience.experience = content;
        return experience;
    }
}
