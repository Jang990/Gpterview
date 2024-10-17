package com.mock.interview.interview.application.helper;

import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.presentation.dto.InterviewOverview;

import java.util.List;

public final class InterviewConvertor {
    private InterviewConvertor() {}

    public static List<InterviewOverview> convert(List<Interview> interview) {
        return interview.stream().map(InterviewConvertor::convert).toList();
    }

    public static InterviewOverview convert(Interview interview) {
        return new InterviewOverview(
                interview.getId(), interview.getTitle().getTitle(),
                interview.getType(), interview.getDurationMinutes(),
                interview.getStartedAt());
    }
}
