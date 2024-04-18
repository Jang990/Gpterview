package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigElements;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigurationCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.ITInterviewTemplateGetter;
import com.mock.interview.tech.presentation.dto.TechnicalSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ITInterviewPromptConfigurator extends ITCategorySupportChecker implements InterviewPromptConfigurator {
    private final ITInterviewTemplateGetter templateGetter;
    private final PromptConfigurationCreator configurationCreator;

    @Override
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.phase()) {
            case TECHNICAL -> createTechPromptConfig(profile, progress.progress());
            case EXPERIENCE -> createExperiencePromptConfig(profile, progress.progress());
            case PERSONAL -> createPersonalPromptConfig(profile, progress.progress());
        };
    }

    @Override
    public String getCurrentTopic(InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.phase()) {
            case TECHNICAL, EXPERIENCE -> {
                TechnicalSubjectsResponse tech = selectStringBasedOnProgress(progress.progress(), profile.skills());
                yield tech == null ? null : tech.getName();
            }
            case PERSONAL -> null;
        };
    }

    private PromptConfiguration createPersonalPromptConfig(InterviewProfile profile, double progress) {
        return configurationCreator.create(
                templateGetter.getPersonal(),
                PromptConfigElements.builder().category(profile.category()).field(profile.field()).build()
        );
    }

    private PromptConfiguration createExperiencePromptConfig(InterviewProfile profile, double progress) {
        ExperienceDto selectedExperience = selectStringBasedOnProgress(progress, profile.experience());
        return configurationCreator.create(
                templateGetter.getExperience(),
                PromptConfigElements.builder().category(profile.category()).field(profile.field()).experience(selectedExperience).build()
        );
    }

    private PromptConfiguration createTechPromptConfig(InterviewProfile profile, double progress) {
        TechnicalSubjectsResponse selectedTech = selectStringBasedOnProgress(progress, profile.skills());
        return configurationCreator.create(
                templateGetter.getTechnical(),
                PromptConfigElements.builder().category(profile.category()).field(profile.field()).tech(selectedTech).build()
        );
    }

    private <T> T selectStringBasedOnProgress(double progress, List<T> list) {
        if(list.isEmpty())
            return null;
        int n = (int) Math.floor(progress * list.size());
        return list.get(n);
    }
}