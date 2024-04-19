package com.mock.interview.interview.infra.progress.dto.topic;

import com.mock.interview.experience.domain.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.user.domain.model.Experience;

public class ExperienceTopic implements InterviewTopic{
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

    public void connectToQuestion(ExperienceRepository repository, InterviewQuestion question) {
        Experience experience = repository.findById(id)
                .orElseThrow(ExperienceNotFoundException::new);
        question.linkExperience(experience);
    }
}
