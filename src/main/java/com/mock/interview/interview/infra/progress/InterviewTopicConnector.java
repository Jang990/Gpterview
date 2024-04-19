package com.mock.interview.interview.infra.progress;

import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interview.infra.progress.dto.topic.ExperienceTopic;
import com.mock.interview.interview.infra.progress.dto.topic.InterviewTopic;
import com.mock.interview.interview.infra.progress.dto.topic.TechTopic;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewTopicConnector {
    private final ExperienceRepository experienceRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    public void connect(InterviewQuestion question, InterviewProgress interviewProgress) {
        InterviewTopic<?> interviewTopic = interviewProgress.interviewTopic();
        switch (interviewProgress.phase()) {
            case TECHNICAL -> {
                if(interviewTopic instanceof TechTopic topic)
                    topic.connectToQuestion(technicalSubjectsRepository, question);
                else
                    throw new IllegalArgumentException("TECHNICAL - TOPIC 연결 오류");
            }
            case EXPERIENCE -> {
                if(interviewTopic instanceof ExperienceTopic topic)
                    topic.connectToQuestion(experienceRepository, question);
                else
                    throw new IllegalArgumentException("EXPERIENCE - TOPIC 연결 오류");
            }
            case PERSONAL -> {}
        }
    }
}
