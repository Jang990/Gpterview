package com.mock.interview.interview.infra;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProgressTopicService {
    public List<? extends InterviewTopic<?>> getTopicList(InterviewPhase phase, InterviewInfo info) {
        return switch (phase) {
            case TECHNICAL -> info.getTechTopics();
            case EXPERIENCE -> info.getExperienceTopics();
            case PERSONAL -> Collections.emptyList();
        };
    }

    public <T extends InterviewTopic<?>> T selectTopic(List<T> topics, double progress) {
        if(topics.isEmpty())
            return null;
        return topics.get(findProgressIndex(progress, topics.size()));
    }

    private int findProgressIndex(double progress, int size) {
        return (int) Math.floor(progress * size);
    }
}
