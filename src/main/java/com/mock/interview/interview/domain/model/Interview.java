package com.mock.interview.interview.domain.model;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.global.Events;
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
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview {
    @Id
    @Column(name = "interview_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private InterviewTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Embedded
    private InterviewTimer timer;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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
            InterviewTimeHolder timeHolder,
            InterviewConfigForm interviewConfig, Users user,
            JobCategory category, JobPosition position
    ) {
        Interview interview = new Interview();
        interview.users = user;
        interview.type = interviewConfig.getInterviewType();
        interview.durationMinutes = interviewConfig.getDurationMinutes();
        interview.timer = createTimer(timeHolder.now(), interviewConfig.getDurationMinutes());

        initCategory(category, position, interview);
        return interview;
    }

    private static InterviewTimer createTimer(LocalDateTime current , int durationMinutes) {
        if(durationMinutes <= 0)
            throw new IllegalArgumentException("면접 시간은 0분 이하일 수 없음.");
        return new InterviewTimer(current, current.plusMinutes(durationMinutes));
    }

    private static void initCategory(JobCategory category, JobPosition position, Interview interview) {
        if(category == null || position == null ||
                !position.getCategory().equals(category))
            throw new IllegalArgumentException("카테고리와 포지션 문제 발생");
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

    public void expire(InterviewTimeHolder timeHolder) {
        verifyTimeoutState(timeHolder);
        if(timer.getStartedAt().isAfter(timeHolder.now()))
            throw new IllegalArgumentException("만료시간을 시작시간 이전으로 설정 불가능");

        timer = timer.withExpiredAt(timeHolder.now());
    }

    public boolean isTimeout(InterviewTimeHolder timeHolder) {
        return timer.isExpired(timeHolder.now());
    }

    public void continueInterview(InterviewTimeHolder timeHolder) {
        if(this.id == null)
            throw new IllegalStateException();
        verifyInterviewTypeRequirement();
        verifyTimeoutState(timeHolder);

        Events.raise(new InterviewContinuedEvent(this.id));
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

    private void verifyTimeoutState(InterviewTimeHolder timeHolder) {
        if(isTimeout(timeHolder))
            throw new IsAlreadyTimeoutInterviewException();
    }

    public InterviewPhase tracePhase(InterviewTimeHolder timeHolder) {
        verifyTimeoutState(timeHolder);

        int currentIdx = findCurrentPhaseIdx(timeHolder);
        return InterviewPhases.getPhaseOrder(type)[currentIdx];
    }

    public double traceProgress(InterviewTimeHolder timeHolder) {
        verifyTimeoutState(timeHolder);

        long eachPhaseDuration = eachPhaseDuration();
        long passedTimeOfCurrentPhase = timePassed(timer.getStartedAt(), timeHolder.now()) % eachPhaseDuration;
        return (double) passedTimeOfCurrentPhase / eachPhaseDuration;
    }

    /** 경과 시간 / 각 페이즈 시간 */
    private int findCurrentPhaseIdx(InterviewTimeHolder holder) {
        return (int) (timePassed(timer.getStartedAt(), holder.now()) / eachPhaseDuration());
    }

    /** 면접_총_시간 / 면접_페이즈_수 */
    private long eachPhaseDuration() {
        return interviewDuration() / InterviewPhases.numberOfPhase(type);
    }

    private long interviewDuration() {
        return timePassed(timer.getStartedAt(), timer.getExpiredAt());
    }

    private long timePassed(LocalDateTime start, LocalDateTime end) {
        return TimeDifferenceCalculator.calculate(ChronoUnit.SECONDS, start, end);
    }
}
