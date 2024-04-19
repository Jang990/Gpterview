package com.mock.interview.interview.infra.progress.dto.topic;

import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.experience.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public record ExperienceTopic(Long id,String content) implements InterviewTopic<Experience> {

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void connectToQuestion(JpaRepository<Experience, Long> repository, InterviewQuestion question) {
        Experience experience = repository.findById(id)
                .orElseThrow(ExperienceNotFoundException::new);
        question.linkExperience(experience);
    }
}
