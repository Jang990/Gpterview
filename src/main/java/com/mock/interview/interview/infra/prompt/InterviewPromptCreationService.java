package com.mock.interview.interview.infra.prompt;

import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.infra.gpt.AISpecification;
import com.mock.interview.interview.infra.prompt.configurator.PromptConfiguration;
import com.mock.interview.interview.infra.prompt.configurator.generator.InterviewPromptConfigurator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewPromptCreationService {
    private final List<InterviewPromptConfigurator> interviewPromptConfiguratorList;
    private final PromptCreator promptCreator;
    private final AISpecification aiSpec;

    public AiPrompt create(InterviewInfo interviewInfo, InterviewProgress interviewProgress, AiQuestionCreator.CreationOption creationOption) {
        PromptConfiguration promptConfig = createPromptConfig(interviewInfo, interviewProgress);
        return createPrompt(promptConfig, creationOption);
    }

    private PromptConfiguration createPromptConfig(InterviewInfo interviewInfo, InterviewProgress progress) {
        InterviewPromptConfigurator configurator = CategoryModuleFinder
                .findModule(interviewPromptConfiguratorList, interviewInfo.profile().category().getName());
        return configurator.configStrategy(interviewInfo.profile(), progress);
    }

    private AiPrompt createPrompt(PromptConfiguration promptConfig, AiQuestionCreator.CreationOption creationOption) {
        return switch (creationOption) {
            case NORMAL -> promptCreator.create(aiSpec, promptConfig);
            case CHANGING_TOPIC -> promptCreator.changeTopic(aiSpec, promptConfig);
        };
    }
}
