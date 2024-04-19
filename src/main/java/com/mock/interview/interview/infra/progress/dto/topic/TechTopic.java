package com.mock.interview.interview.infra.progress.dto.topic;

import com.mock.interview.experience.domain.ExperienceNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import org.springframework.data.jpa.repository.JpaRepository;

public record TechTopic(Long id,String content) implements InterviewTopic<TechnicalSubjects> {
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void connectToQuestion(JpaRepository<TechnicalSubjects, Long> repository, InterviewQuestion question) {
        TechnicalSubjects technicalSubjects = repository.findById(id)
                .orElseThrow(ExperienceNotFoundException::new);
        question.linkTech(technicalSubjects);
    }
}
