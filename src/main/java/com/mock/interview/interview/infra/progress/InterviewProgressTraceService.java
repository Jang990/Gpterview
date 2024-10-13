package com.mock.interview.interview.infra.progress;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/** 현재 면접 주제 파악 서비스 */
@Service
@RequiredArgsConstructor
public class InterviewProgressTraceService {
    private final ProgressTracker progressTracker;

    public InterviewProgress trace(InterviewInfo info) {
        LocalDateTime current = LocalDateTime.now();
        return new InterviewProgress(
                tracePhase(current, info),
                traceTopic(current, info),
                traceProgress(current, info)
        );
    }

    private InterviewPhase tracePhase(LocalDateTime time, InterviewInfo info) {
        return progressTracker.tracePhase(time, info.config());
    }

    private double traceProgress(LocalDateTime time, InterviewInfo info) {
        return progressTracker.traceProgress(time, info.config());
    }

    private InterviewTopic<?> traceTopic(LocalDateTime current, InterviewInfo info) {
        return switch (tracePhase(current, info)) {
            case TECHNICAL -> selectTopic(
                    info.getTechTopics(),
                    traceProgress(current, info)
            );
            case EXPERIENCE -> selectTopic(
                    info.getExperienceTopics(),
                    traceProgress(current, info)
            );
            case PERSONAL -> null;
        };
    }

    private <T extends InterviewTopic<?>> T selectTopic(List<T> topics, double progress) {
        if(topics.isEmpty())
            return null;
        return topics.get(findProgressIndex(progress, topics.size()));
    }

    private int findProgressIndex(double progress, int size) {
        return (int) Math.floor(progress * size);
    }

}
