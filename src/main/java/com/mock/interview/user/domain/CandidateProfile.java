package com.mock.interview.user.domain;

import com.mock.interview.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidateProfile {
    @Id
    @Column(name = "candidate_profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO -1: {분야,직군,스킬}을 따로 테이블로 빼고 프론트에서 검색창처럼 지원해줄 것. Category enum 같은 방식
    // TODO -2: 테이블에 없는 {분야,직군,스킬}이라면 커스텀으로 만들 수 있게도 해줄 것.
    private String department;  // "개발", "영업", "세무"
    private String field; // BE, FE, 디자인
    private String skills; // Java, Spring, Mysql, Jenkins ...
    private String experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users users;
}
