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
    private final InterviewProgressTimeBasedTracker progressTracker;

    public InterviewProgress trace(InterviewInfo interviewInfo) {
        LocalDateTime now = LocalDateTime.now();
        InterviewPhase phase = progressTracker.tracePhase(now, interviewInfo.config());
        double progress = progressTracker.traceProgress(now, interviewInfo.config());
        InterviewTopic currentTopic = getCurrentTopic(phase, progress, interviewInfo.profile());
        return new InterviewProgress(phase, currentTopic, progress);
    }

    private InterviewTopic getCurrentTopic(InterviewPhase phase, double progress, InterviewProfile profile) {
        return switch (phase) {
            case TECHNICAL -> selectStringBasedOnProgress(progress, profile.skills());
            case EXPERIENCE -> selectStringBasedOnProgress(progress, profile.experience());
            case PERSONAL -> null;
        };
    }

    private <T extends InterviewTopic> T selectStringBasedOnProgress(double progress, List<T> list) {
        if(list.isEmpty())
            return null;
        int n = (int) Math.floor(progress * list.size());
        return list.get(n);
    }

}
