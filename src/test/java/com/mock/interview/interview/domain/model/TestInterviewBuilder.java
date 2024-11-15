package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.user.domain.model.Users;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class TestInterviewBuilder {
    private static final LocalDateTime NOW = LocalDateTime.now();
    public static final int DEFAULT_DURATION_MINUTE = 30;
    public static final LocalDateTime DEFAULT_START_AT = NOW;
    public static final LocalDateTime DEFAULT_EXPIRED_AT = NOW.plusMinutes(DEFAULT_DURATION_MINUTE);
    public static final InterviewType DEFAULT_INTERVIEW_TYPE = InterviewType.PERSONALITY;

    public static TestInterviewBuilder builder() {
        return new TestInterviewBuilder();
    }

    private int durationMinute;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;
    private InterviewConfigForm config = new InterviewConfigForm();
    private JobCategory category = mock(JobCategory.class);
    private JobPosition position = mock(JobPosition.class);
    private Users user = mock(Users.class);
    private List<TechnicalSubjects> techTopics = List.of(mock(TechnicalSubjects.class));
    private List<Experience> experiencesTopics = List.of(mock(Experience.class));

    public TestInterviewBuilder() {
        interviewType(DEFAULT_INTERVIEW_TYPE);
        durationMinute(DEFAULT_DURATION_MINUTE);
        timer(DEFAULT_DURATION_MINUTE, DEFAULT_START_AT, DEFAULT_EXPIRED_AT);
    }

    public TestInterviewBuilder timer(int durationMinute, LocalDateTime startedAt, LocalDateTime expiredAt) {
        this.durationMinute = durationMinute;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        return this;
    }

    public TestInterviewBuilder durationMinute(int duration) {
        config = new InterviewConfigForm(config.getInterviewType(), duration);
        return this;
    }

    public TestInterviewBuilder interviewType(InterviewType type) {
        config = new InterviewConfigForm(type, config.getDurationMinutes());
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
        return Interview.create(
                mock(InterviewTitle.class),
                new InterviewTimer(durationMinute, startedAt, expiredAt), config,
                new CandidateInfo(user, category, position),
                InterviewTopicDto.builder()
                        .techTopics(techTopics)
                        .experienceTopics(experiencesTopics)
                        .build()
        );
    }
}
