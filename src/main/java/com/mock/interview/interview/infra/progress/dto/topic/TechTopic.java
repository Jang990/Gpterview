package com.mock.interview.interview.infra.progress.dto.topic;

import com.mock.interview.experience.domain.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.domain.model.Experience;

public class TechTopic implements InterviewTopic {
    private Long id;
    private String content;
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return content;
    }

    public void connectToQuestion(TechnicalSubjectsRepository repository, InterviewQuestion question) {
        TechnicalSubjects technicalSubjects = repository.findById(id)
                .orElseThrow(ExperienceNotFoundException::new);
        question.linkTech(technicalSubjects);
    }
}
