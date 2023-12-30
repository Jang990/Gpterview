package com.mock.interview.interview.domain;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.user.domain.CandidateProfile;
import com.mock.interview.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview extends BaseTimeEntity {
    @Id
    @Column(name = "interview_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateProfile candidateProfile;
}
