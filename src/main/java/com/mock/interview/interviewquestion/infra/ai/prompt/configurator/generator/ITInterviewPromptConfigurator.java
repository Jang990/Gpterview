package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.experience.presentation.dto.api.ExperienceResponse;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.InterviewTopic;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigElements;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
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
        String promptTemplate = getTemplate(progress.phase());
        String topic = getTopic(profile, progress);
        PromptConfigElements elements = new PromptConfigElements(profile.category().getName(), profile.field().getName(), topic);
        return configurationCreator.create(promptTemplate, elements);
    }

    private String getTemplate(InterviewPhase phase) {
        return switch (phase) {
            case TECHNICAL -> templateGetter.getTechnical();
            case EXPERIENCE -> templateGetter.getExperience();
            case PERSONAL -> templateGetter.getPersonal();
        };
    }

    public String getTopic(InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.phase()) {
            case TECHNICAL -> selectStringBasedOnProgress(progress.progress(), profile.skills()).getName();
            case EXPERIENCE -> selectStringBasedOnProgress(progress.progress(), profile.experience()).getName();
            case PERSONAL -> null;
        };
    }

    @Override
    public InterviewTopic getCurrentTopic(InterviewProfile profile, InterviewProgress progress) {
        return switch (progress.phase()) {
            case TECHNICAL, EXPERIENCE -> selectStringBasedOnProgress(progress.progress(), profile.skills());
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