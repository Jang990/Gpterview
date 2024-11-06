package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.interview.domain.InterviewTimeHolder;
import com.mock.interview.interview.presentation.dto.InterviewConfigForm;
import com.mock.interview.interview.presentation.dto.InterviewType;
import com.mock.interview.user.domain.model.Users;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestInterviewBuilder {
    public static final String DEFAULT_CATEGORY_NAME = "MyCategory";
    public static final String DEFAULT_POSITION_NAME = "MyBackendPosition";
    public static final int DEFAULT_DURATION_MINUTE = 30;
    public static final LocalDateTime DEFAULT_START_AT = LocalDateTime.now();
    public static final InterviewType DEFAULT_INTERVIEW_TYPE = InterviewType.TECHNICAL;

    public static TestInterviewBuilder builder() {
        return new TestInterviewBuilder();
    }

    private InterviewTimeHolder timeHolder = mock(InterviewTimeHolder.class);
    private InterviewConfigForm config = new InterviewConfigForm();
    private JobCategory category = mock(JobCategory.class);
    private JobPosition position = mock(JobPosition.class);
    private Users user = mock(Users.class);

    public TestInterviewBuilder() {
        startAt(DEFAULT_START_AT);
        interviewType(DEFAULT_INTERVIEW_TYPE);
        durationMinute(DEFAULT_DURATION_MINUTE);
        jobDetail(DEFAULT_CATEGORY_NAME, DEFAULT_POSITION_NAME);
    }

    public TestInterviewBuilder jobDetail(String categoryName, String positionName) {
        when(category.getName()).thenReturn(categoryName);

        when(position.getName()).thenReturn(positionName);
        when(position.getCategory()).thenReturn(category);
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

    public TestInterviewBuilder startAt(LocalDateTime startAt) {
        when(timeHolder.now()).thenReturn(startAt);
        return this;
    }


    public Interview build() {
        return Interview.create(
                timeHolder, config,
                user, category, position
        );
    }
}
