package com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator;

import com.mock.interview.category.infra.support.DefaultCategorySupportChecker;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.PromptConfigurationCreator;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.template.DefaultInterviewTemplateGetter;
import lombok.RequiredArgsConstructor;

//@Order(1)
//@Component
@RequiredArgsConstructor
public class DefaultInterviewPromptConfigurator extends DefaultCategorySupportChecker {
    private final DefaultInterviewTemplateGetter templateGetter;
    private final PromptConfigurationCreator configurationCreator;

    // TODO: 수정 많이 필요.
    //      -> IT 먼저 끝내고 바꿀 것.
    /*@Override
    public PromptConfiguration configStrategy(AISpecification aiSpec, InterviewProfile profile, InterviewProgress progress) {
        String rawStrategy = getRawInterviewStrategy(progress.phase());
        return null;
    }

    @Override
    public InterviewTopic getCurrentTopic(InterviewProfile profile, InterviewProgress progress) {
        return null;
    }

    private String getRawInterviewStrategy(InterviewPhase phase) {
        return switch (phase) {
            case TECHNICAL -> templateGetter.getTechnical();
            case EXPERIENCE -> templateGetter.getExperience();
            case PERSONAL -> templateGetter.getPersonal();
        };
    }*/
}