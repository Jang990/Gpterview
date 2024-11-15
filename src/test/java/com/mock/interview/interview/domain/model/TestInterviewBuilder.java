package com.mock.interview.interview.domain.model;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.TimeUtils;
import com.mock.interview.interview.application.dto.InterviewTopicDto;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.tech.domain.model.TechnicalSubjects;

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
    private InterviewConfigForm config = new InterviewConfigForm();
    private List<TechnicalSubjects> techTopics = List.of(mock(TechnicalSubjects.class));
    private List<Experience> experiencesTopics = List.of(mock(Experience.class));

    public TestInterviewBuilder() {
        interviewType(DEFAULT_INTERVIEW_TYPE);
        timer(DEFAULT_START_AT, DEFAULT_EXPIRED_AT);
    }

    public TestInterviewBuilder timer(LocalDateTime startedAt, LocalDateTime expiredAt) {
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.durationMinute = (int) Duration.between(startedAt, expiredAt).toMinutes();
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
                mock(CandidateInfo.class),
                InterviewTopicDto.builder()
                        .techTopics(techTopics)
                        .experienceTopics(experiencesTopics)
                        .build()
        );
    }
}
