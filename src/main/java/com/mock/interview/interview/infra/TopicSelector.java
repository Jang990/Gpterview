package com.mock.interview.interview.infra;

import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicSelector {
    public <T extends InterviewTopic<?>> T selectTopic(List<T> topics, double progress) {
        if(topics.isEmpty())
            return null;
        return topics.get(findProgressIndex(progress, topics.size()));
    }

    private int findProgressIndex(double progress, int size) {
        return (int) Math.floor(progress * size);
    }
}
