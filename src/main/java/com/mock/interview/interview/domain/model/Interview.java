package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.event.InterviewContinuedEvent;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.user.domain.model.Users;
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
    private boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime expiredTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id")
    private CandidateConfig candidateConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id")
    private JobPosition position;

    public static Interview startInterview(CandidateConfig config, Users user, JobPosition position) {
        Interview interview = new Interview();
        interview.title = new InterviewTitle(config.getCategory().getName(), config.getPosition().getName());
        LocalDateTime now = LocalDateTime.now();
        interview.expiredTime = now.plusMinutes(config.getDurationMinutes());
        interview.isDeleted = false;
        interview.users = user;
        interview.candidateConfig = config;
        interview.category = position.getCategory();
        interview.position = position;
        return interview;
    }

    // TODO: 사용자가 면접을 강제 종료할 수 있게 만들어야 함.
    public void expire() {
        if(isTimeout())
            throw new IsAlreadyTimeoutInterviewException();

        expiredTime = LocalDateTime.now();
    }

    public boolean isTimeout() {
        return expiredTime.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return !isTimeout();
    }

    public boolean continueInterview() {
        if(isTimeout())
            return false;

        Events.raise(new InterviewContinuedEvent(this.id));
        return true;
    }
}
