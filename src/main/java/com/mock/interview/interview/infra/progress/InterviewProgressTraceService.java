package com.mock.interview.interview.infra.progress;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
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

    public InterviewProgress trace(InterviewInfo interviewInfo) {
        LocalDateTime now = LocalDateTime.now();
        InterviewPhase phase = progressTracker.tracePhase(now, interviewInfo.config());
        double progress = progressTracker.traceProgress(now, interviewInfo.config());
        InterviewTopic<?> currentTopic = getCurrentTopic(phase, progress, interviewInfo.profile());
        return new InterviewProgress(phase, currentTopic, progress);
    }

    private InterviewTopic<?> getCurrentTopic(InterviewPhase phase, double progress, InterviewProfile profile) {
        return switch (phase) {
            case TECHNICAL -> selectTopic(profile.skills(), progress);
            case EXPERIENCE -> selectTopic(profile.experience(), progress);
            case PERSONAL -> null;
        };
    }

    private <T extends InterviewTopic<?>> T selectTopic(List<T> topics, double progress) {
        if(topics.isEmpty())
            return null;
        int topicIdx = (int) Math.floor(progress * topics.size());
        return topics.get(topicIdx);
    }

}
