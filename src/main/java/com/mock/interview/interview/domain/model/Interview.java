package com.mock.interview.interview.domain.model;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.global.TimeDifferenceCalculator;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.domain.InterviewTopicsDto;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
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
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Embedded
    private InterviewTimer timer;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Embedded
    private CandidateInfo candidateInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType type;

    @Embedded
    private InterviewTopics topics;

    protected static Interview create(
            InterviewTitle title,
            InterviewTimer timer,
            CandidateInfo candidateInfo,
            InterviewTopicsDto topicDto
    ) {
        Interview interview = new Interview();
        interview.title = title;
        interview.timer = timer;
        interview.candidateInfo = candidateInfo;
        interview.type = topicDto.getType();

        interview.topics = new InterviewTopics();
        interview.addTechTopics(topicDto.getTechTopics());
        interview.addExperienceTopics(topicDto.getExperienceTopics());
        return interview;
    }

    public List<TechnicalSubjects> getTechTopics() {
        return topics.getTechLink().stream()
                .map(InterviewTechLink::getTechnicalSubjects)
                .toList();
    }

    public List<Experience> getExperienceTopics() {
        return topics.getExperienceLink().stream()
                .map(InterviewExperienceLink::getExperience)
                .toList();
    }

    public void expire(InterviewTimeHolder timeHolder) {
        LocalDateTime now = timeHolder.now();
        verifyTimeoutState(now);
        if(timer.getStartedAt().isAfter(now))
            throw new IllegalArgumentException("만료시간을 시작시간 이전으로 설정 불가능");

        timer = timer.withExpiredAt(now);
    }

    public boolean isTimeout(LocalDateTime now) {
        return timer.isExpired(now);
    }

    public void continueInterview(LocalDateTime now) {
        if(this.id == null)
            throw new IllegalStateException();
        verifyTimeoutState(now);

        Events.raise(new InterviewContinuedEvent(this.id));
    }

    public String getTitle() {
        return title.getTitle();
    }

    private void addTechTopics(List<TechnicalSubjects> techList) {
        techList.stream()
                .map(tech -> InterviewTechLink.createLink(this, tech))
                .forEach(topics.getTechLink()::add);
    }

    private void addExperienceTopics(List<Experience> experienceList) {
        experienceList.stream()
                .map(experience -> InterviewExperienceLink.createLink(this, experience))
                .forEach(topics.getExperienceLink()::add);
    }

    private void verifyTimeoutState(LocalDateTime now) {
        if(isTimeout(now))
            throw new IsAlreadyTimeoutInterviewException();
    }

    public int getDurationMinutes() {
        return timer.getDurationMinutes();
    }

    public InterviewProgress traceProgress(LocalDateTime now) {
        return new InterviewProgress(
                tracePhase(now),
                traceProgressOfCurrentPhase(now)
        );
    }

    public JobCategory getCategory() {
        return candidateInfo.getCategory();
    }

    public Users getUsers() {
        return candidateInfo.getUsers();
    }

    public JobPosition getPosition() {
        return candidateInfo.getPosition();
    }

    private InterviewPhase tracePhase(LocalDateTime now) {
        verifyTimeoutState(now);

        int currentPhaseIdx = findCurrentPhaseIdx(now);
        return InterviewPhases.getPhaseOrder(type)[currentPhaseIdx];
    }

    private ProgressPercent traceProgressOfCurrentPhase(LocalDateTime now) {
        verifyTimeoutState(now);

        long eachPhaseDuration = eachPhaseDuration();
        long passedTimeOfCurrentPhase = timePassed(timer.getStartedAt(), now) % eachPhaseDuration;
        return new ProgressPercent((double) passedTimeOfCurrentPhase / eachPhaseDuration);
    }

    /** 경과 시간 / 각 페이즈 시간 */
    private int findCurrentPhaseIdx(LocalDateTime now) {
        return (int) (timePassed(timer.getStartedAt(), now) / eachPhaseDuration());
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
