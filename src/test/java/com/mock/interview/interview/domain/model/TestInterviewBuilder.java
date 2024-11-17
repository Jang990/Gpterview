package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.TimeUtils;
import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestInterviewBuilder {
    private static final LocalDateTime DEFAULT_START_AT = TimeUtils.time(0, 0);
    private static final LocalDateTime DEFAULT_EXPIRED_AT = TimeUtils.time(0, 30);
    private static final InterviewType DEFAULT_INTERVIEW_TYPE = InterviewType.PERSONALITY;

    public static TestInterviewBuilder builder() {
        return new TestInterviewBuilder();
    }

    private int durationMinute;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;
    private InterviewType type;
    private List<TechnicalSubjects> techTopics;
    private List<Experience> experiencesTopics;

    private Users users;
    private JobCategory category = mock(JobCategory.class);
    private JobPosition position = mock(JobPosition.class);

    public TestInterviewBuilder() {
        interviewType(DEFAULT_INTERVIEW_TYPE);
        timer(DEFAULT_START_AT, DEFAULT_EXPIRED_AT);
        users(mock(Users.class));
        techTopics(List.of(mock(TechnicalSubjects.class)));
        experienceTopics(List.of(mock(Experience.class)));
    }

    public TestInterviewBuilder timer(LocalDateTime startedAt, LocalDateTime expiredAt) {
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.durationMinute = (int) Duration.between(startedAt, expiredAt).toMinutes();
        return this;
    }

    public TestInterviewBuilder interviewType(InterviewType type) {
        this.type = type;
        return this;
    }

    public TestInterviewBuilder users(Users users) {
        this.users = users;
        return this;
    }

    public TestInterviewBuilder category(JobCategory category) {
        this.category = category;
        return this;
    }

    public TestInterviewBuilder position(JobPosition position) {
        this.position = position;
        return this;
    }

    public TestInterviewBuilder techTopics(List<TechnicalSubjects> techTopics) {
        this.techTopics = techTopics;
        return this;
    }

    public TestInterviewBuilder experienceTopics(List<Experience> experienceTopics) {
        this.experiencesTopics = experienceTopics;
        return this;
    }

    public Interview build() {
        return Interview.createNEW(
                mock(InterviewTitle.class),
                new InterviewTimer(durationMinute, startedAt, expiredAt),
                new CandidateInfo(users, category, position),
                InterviewTopicDto.builder()
                        .type(type)
                        .techTopics(techTopics)
                        .experienceTopics(experiencesTopics)
                        .build()
        );
    }
}
