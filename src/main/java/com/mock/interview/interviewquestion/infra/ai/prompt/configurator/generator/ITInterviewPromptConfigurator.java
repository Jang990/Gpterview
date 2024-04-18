package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import com.mock.interview.interviewquestion.infra.cache.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigurationCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.ITInterviewTemplateGetter;
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
            case TECHNICAL, EXPERIENCE -> selectStringBasedOnProgress(progress.progress(), profile.skills());
            case PERSONAL -> null;
        };
    }

    private PromptConfiguration createPersonalPromptConfig(InterviewProfile profile, double progress) {
        return configurationCreator.create(
                templateGetter.getPersonal(),
                new InterviewProfile(profile.category(), profile.field(), profile.skills(), profile.experience())
        );
    }

    private PromptConfiguration createExperiencePromptConfig(InterviewProfile profile, double progress) {
        String selectedExperience = selectStringBasedOnProgress(progress, profile.experience());
        return configurationCreator.create(
                templateGetter.getExperience(),
                new InterviewProfile(profile.category(), profile.field(), profile.skills(), List.of(selectedExperience))
        );
    }

    private PromptConfiguration createTechPromptConfig(InterviewProfile profile, double progress) {
        String selectedSkills = selectStringBasedOnProgress(progress, profile.skills());
        return configurationCreator.create(
                templateGetter.getTechnical(),
                new InterviewProfile(profile.category(), profile.field(), List.of(selectedSkills), profile.experience())
        );
    }

    private String selectStringBasedOnProgress(double progress, List<String> list) {
        if(list.isEmpty())
            return null;
        int n = (int) Math.floor(progress * list.size());
        return list.get(n);
    }
}