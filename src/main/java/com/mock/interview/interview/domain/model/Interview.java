package com.mock.interview.interview.domain.model;

import com.mock.interview.user.domain.model.Experience;
import com.mock.interview.candidate.presentation.dto.InterviewConfigDto;
import com.mock.interview.candidate.presentation.dto.InterviewType;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.event.InterviewContinuedEvent;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType type;
    @Column(nullable = false)
    private int durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id")
    private JobPosition position;

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "interview")
    private List<InterviewTechLink> techLink = new ArrayList<>();

    @Cascade(CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "interview")
    private List<InterviewExperienceLink> experienceLink = new ArrayList<>();

    public static Interview startInterview(
            InterviewConfigDto interviewConfig,
            Users user, JobCategory category, JobPosition position
    ) {
        Interview interview = new Interview();
        interview.users = user;
        interview.type = interviewConfig.getInterviewType();
        initCategory(category, position, interview);
        initExpiredTime(interview, interviewConfig.getDurationMinutes());
        interview.isDeleted = false;
        return interview;
    }

    public void linkTech(List<TechnicalSubjects> techList) {
        if(techList == null || techList.isEmpty())
            throw new IllegalArgumentException();

        techList.forEach(this::linkTech);
    }

    public void linkTech(TechnicalSubjects tech) {
        if(tech == null)
            throw new IllegalArgumentException();

        techLink.add(InterviewTechLink.createLink(this, tech));
    }

    public void linkExperience(List<Experience> experienceList) {
        if(experienceList == null || experienceList.isEmpty())
            throw new IllegalArgumentException();

        experienceList.forEach(this::linkExperience);
    }

    public void linkExperience(Experience experience) {
        if(experience == null)
            throw new IllegalArgumentException();

        experienceLink.add(InterviewExperienceLink.createLink(this, experience));
    }

    private static void initExpiredTime(Interview interview, int durationMinutes) {
        interview.durationMinutes = durationMinutes;
        interview.expiredTime = LocalDateTime.now().plusMinutes(durationMinutes);
    }

    private static void initCategory(JobCategory category, JobPosition position, Interview interview) {
        interview.category = category;
        interview.position = position;
        interview.title = new InterviewTitle(category.getName(), position.getName());
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
