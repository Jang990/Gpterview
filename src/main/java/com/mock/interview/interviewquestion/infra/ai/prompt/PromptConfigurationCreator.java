package com.mock.interview.interviewquestion.infra.ai.prompt;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewProfile;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.validator.PromptTemplateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptConfigurationCreator {
    private final PromptTemplateValidator templateValidator;

    public PromptConfiguration create(String promptTemplate, InterviewProfile profile) {
        templateValidator.verify(promptTemplate, profile);

        /*
        TODO: 여기서 PromptConfiguration 생성자를 default로 바꿨음. 이로 인한 사이드 이펙트 수정해야함.
            근데 여기 create에서 받는 InterviewProfile을 PromptConfigurationParams을 생성해서 수정할것
        */
        return new PromptConfiguration(
                promptTemplate, profile.department(), profile.field(),
                profile.skills().toString(), profile.experience().toString()
        );
    }
}
