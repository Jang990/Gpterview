package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.category.infra.support.ITCategorySupportChecker;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.TraceResult;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigElements;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfiguration;
import com.mock.interview.interviewquestion.infra.ai.gpt.AISpecification;
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
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, TraceResult progress) {
        String promptTemplate = getTemplate(progress.phase());
        String topic = progress.getTopicContent();
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
}