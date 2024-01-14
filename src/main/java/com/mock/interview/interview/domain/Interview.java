package com.mock.interview.interview.domain;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview extends BaseTimeEntity {
    @Id
    @Column(name = "interview_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private InterviewTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateConfig candidateConfig;

    public static Interview startInterview(CandidateConfig config, Users user) {
        Interview interview = new Interview();
        interview.title = new InterviewTitle(config.getDepartment().getName(), config.getAppliedJob().getName());
        LocalDateTime now = LocalDateTime.now();
        interview.endTime = now.plusMinutes(config.getDurationMinutes());
        interview.isActive = true;
        interview.isDeleted = false;
        interview.users = user;
        interview.candidateConfig = config;

        return interview;
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(endTime);
    }

    public void timeout() {
        if(!this.isActive)
            throw new IsAlreadyTimeoutInterviewException();

        this.isActive = false;
    }
}
