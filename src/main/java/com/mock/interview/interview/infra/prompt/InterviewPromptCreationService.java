package com.mock.interview.interview.infra.prompt;

import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.cache.dto.InterviewProfile;
import com.mock.interview.interview.infra.progress.dto.InterviewPhase;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interview.infra.prompt.configurator.PromptConfigElements;
import com.mock.interview.interview.infra.prompt.configurator.PromptConfigurationCreator;
import com.mock.interview.interview.infra.prompt.configurator.template.InterviewPromptTemplate;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.infra.gpt.AISpecification;
import com.mock.interview.interview.infra.prompt.configurator.PromptConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewPromptCreationService {
    private final List<InterviewPromptTemplate> interviewPromptTemplateList;
    private final PromptCreator promptCreator;
    private final PromptConfigurationCreator configurationCreator;
    private final AISpecification aiSpec;

    public AiPrompt create(InterviewInfo interviewInfo, InterviewProgress interviewProgress, AiQuestionCreator.CreationOption creationOption) {
        PromptConfiguration promptConfig = createPromptConfig(interviewInfo.profile(), interviewProgress);
        return createPrompt(promptConfig, creationOption);
    }

    private PromptConfiguration createPromptConfig(InterviewProfile profile, InterviewProgress progress) {
        String template = getPromptTemplate(progress.phase(), profile.category().getName());
        PromptConfigElements elements = createPromptElements(profile, progress.getTopicContent());
        return configurationCreator.create(template, elements);
    }

    private String getPromptTemplate(InterviewPhase phase, String categoryName) {
        InterviewPromptTemplate promptTemplate = CategoryModuleFinder
                .findModule(interviewPromptTemplateList, categoryName);
        return promptTemplate.getTemplate(phase);
    }

    private PromptConfigElements createPromptElements(InterviewProfile profile, String topic) {
        return new PromptConfigElements(
                profile.category().getName(),
                profile.field().getName(),
                topic
        );
    }


    private AiPrompt createPrompt(PromptConfiguration promptConfig, AiQuestionCreator.CreationOption creationOption) {
        return switch (creationOption) {
            case NORMAL -> promptCreator.create(aiSpec, promptConfig);
            case CHANGING_TOPIC -> promptCreator.changeTopic(aiSpec, promptConfig);
        };
    }
}
