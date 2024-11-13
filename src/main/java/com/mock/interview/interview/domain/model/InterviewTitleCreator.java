package com.mock.interview.interview.domain.model;

import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import org.springframework.stereotype.Service;

@Service
public class InterviewTitleCreator {
    public InterviewTitle createDefault(JobCategory category, JobPosition position) {
        return new InterviewTitle(category.getName(), position.getName());
    }
}
