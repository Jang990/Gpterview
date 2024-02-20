package com.mock.interview.candidate.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/*
    AI 프롬프트를 만들 때 중복된 경험을 만들 수 있는데 순서 정보를 포함해야 하지 않을까?
    = createdAt의 저장 시간으로 정렬해서 사용하면 된다.
    마이크로초까지 저장되기 때문에 중복되서 저장될 확률이 낮다.
*/
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
