package com.mock.interview.interview.infra.progress;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/** 현재 면접 주제 파악 서비스 */
@Service
@RequiredArgsConstructor
public class InterviewProgressTraceService {
    private final ProgressTracker progressTracker;
    private final ProgressTopicService progressTopicService;

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
        return progressTopicService.selectTopic(
                progressTopicService.findTopicList(tracePhase(current, info), info),
                traceProgress(current, info)
        );
    }
}
