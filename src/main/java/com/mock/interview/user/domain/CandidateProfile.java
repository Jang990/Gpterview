package com.mock.interview.user.domain;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.presentation.dto.CandidateProfileDto;
import com.mock.interview.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidateProfile extends BaseTimeEntity {
    @Id
    @Column(name = "candidate_profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    // TODO -1: {분야,직군,스킬}을 따로 테이블로 빼고 프론트에서 검색창처럼 지원해줄 것. Category enum 같은 방식
    // TODO -2: 테이블에 없는 {분야,직군,스킬}이라면 커스텀으로 만들 수 있게도 해줄 것.
    private String department;  // "개발", "영업", "세무"
    private String field; // BE, FE, 디자인
    private String skills; // Java, Spring, Mysql, Jenkins ...
    private String experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users users;

    // TODO: Convertor를 따로 만들어서 빼야 하나? or 팩토리 메소드를 유지해야 하나?
    public static CandidateProfile createProfile(CandidateProfileDto profileDto, Users users) {
        CandidateProfile candidateProfile = new CandidateProfile();
        candidateProfile.title = "새 프로필";
        candidateProfile.department = profileDto.getDepartment();
        candidateProfile.field = profileDto.getField();
        candidateProfile.skills = profileDto.getSkills();
        candidateProfile.experience = profileDto.getExperience();
        candidateProfile.users = users;

        return candidateProfile;
    }
}
