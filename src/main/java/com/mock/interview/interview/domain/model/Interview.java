package com.mock.interview.interview.domain.model;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
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

import java.time.LocalDate;
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
    @OneToMany(mappedBy = "interview")
    private List<InterviewTechLink> techLink = new ArrayList<>();

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "interview")
    private List<InterviewExperienceLink> experienceLink = new ArrayList<>();

    public static Interview create(
            InterviewConfigForm interviewConfig, Users user,
            JobCategory category, JobPosition position
    ) {
        Interview interview = new Interview();
        interview.users = user;
        interview.type = interviewConfig.getInterviewType();
        initCategory(category, position, interview);
        initExpiredTime(interview, interviewConfig.getDurationMinutes());
        return interview;
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

    public void linkTech(List<TechnicalSubjects> techList) {
        if(techList == null || techList.isEmpty())
            throw new IllegalArgumentException();

        techList.forEach(this::linkTech);
    }

    public void linkTech(TechnicalSubjects tech) {
        if(tech == null)
            throw new IllegalArgumentException();
        if(type != InterviewType.TECHNICAL)
            throw new IllegalStateException();

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
        if(type != InterviewType.EXPERIENCE)
            throw new IllegalStateException();

        experienceLink.add(InterviewExperienceLink.createLink(this, experience));
    }

    public void expire() {
        verifyTimeoutState();

        expiredTime = LocalDateTime.now();
    }

    public boolean isTimeout() {
        return expiredTime.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return !isTimeout();
    }

    public void continueInterview() {
        verifyReadyToContinue();
        Events.raise(new InterviewContinuedEvent(this.id));
    }

    private void verifyReadyToContinue() {
        verifyIdExists();
        verifyCategoryPositionExists();
        verifyInterviewTypeRequirement();
        verifyTimeoutState();
    }

    private void verifyIdExists() {
        if(this.id == null)
            throw new IllegalStateException();
    }

    private void verifyCategoryPositionExists() {
        if(category == null || position == null)
            throw new IllegalStateException("면접 진행 시 분야-직무는 필수");
    }

    private void verifyInterviewTypeRequirement() {
        switch (type) {
            case TECHNICAL -> {
                if(techLink == null || techLink.isEmpty())
                    throw new IllegalStateException("기술 면접은 기술이 필수");
            }
            case EXPERIENCE -> {
                if(experienceLink == null || experienceLink.isEmpty())
                    throw new IllegalStateException("경험 면접은 경험이 필수");
            }
            case PERSONALITY -> { /* 딱히 검증할 것 없음*/}
            default -> throw new IllegalStateException("지원하지 않는 면접 타입");
        }
    }

    private void verifyTimeoutState() {
        if(isTimeout())
            throw new IsAlreadyTimeoutInterviewException();
    }
}
