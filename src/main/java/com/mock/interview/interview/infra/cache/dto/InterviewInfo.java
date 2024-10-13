package com.mock.interview.interview.infra.cache.dto;

import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;

import java.util.List;

public record InterviewInfo(Long interviewId, InterviewProfile profile, InterviewConfig config) {
    public List<TechTopic> getTechTopics() {
        return profile.skills();
    }

    public List<ExperienceTopic> getExperienceTopics() {
        return profile.experience();
    }

}