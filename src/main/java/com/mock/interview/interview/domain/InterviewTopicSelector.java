package com.mock.interview.interview.domain;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewTopicSelector {
    private final InterviewTimeHolder timeHolder;

    public InterviewTopic select(
            Interview interview,
            List<TechnicalSubjects> relatedTechs,
            List<Experience> relatedExperience) {
        LocalDateTime selectedTime = timeHolder.now();
        if(interview.tracePhase(timeHolder) == InterviewPhase.PERSONAL)
            return InterviewTopic.createEmptyTopic(selectedTime);

        long selectedTopicId = findTopicId(interview, relatedTechs, relatedExperience);
        return InterviewTopic.create(selectedTime, selectedTopicId);
    }

    private long findTopicId(
            Interview interview,
            List<TechnicalSubjects> relatedTechs,
            List<Experience> relatedExperience) {
        double progress = interview.traceProgress(timeHolder);
        return switch (interview.tracePhase(timeHolder)) {
            case TECHNICAL -> selectTopic(toTechIds(relatedTechs), progress);
            case EXPERIENCE -> selectTopic(toExperienceIds(relatedExperience), progress);
            case PERSONAL -> throw new IllegalStateException("지원하지 않는 면접 주제");
        };
    }

    private List<Long> toTechIds(List<TechnicalSubjects> techs) {
        return techs.stream()
                .mapToLong(TechnicalSubjects::getId)
                .boxed()
                .toList();
    }

    private List<Long> toExperienceIds(List<Experience> experiences) {
        return experiences.stream()
                .mapToLong(Experience::getId)
                .boxed()
                .toList();
    }

    private long selectTopic(List<Long> ids, double progress) {
        return ids.get(findCurrentIndex(progress, ids.size()));
    }

    private int findCurrentIndex(double progress, int size) {
        return (int) Math.floor(progress * size);
    }
}
