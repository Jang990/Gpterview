package com.mock.interview.interviewquestion.infra.ai.progress;

import com.mock.interview.category.infra.CategoryModuleFinder;
import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.prompt.configurator.generator.InterviewPromptConfigurator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/** 현재 면접 주제 파악 서비스 */
@Service
@RequiredArgsConstructor
public class CurrentTopicTracker {
    private final List<InterviewPromptConfigurator> interviewPromptConfiguratorList;
    private final InterviewProgressTimeBasedTracker progressTracker;

    public TraceResult trace(InterviewInfo interviewInfo) {
        InterviewProgress progress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        InterviewPromptConfigurator configurator = CategoryModuleFinder
                .findModule(interviewPromptConfiguratorList, interviewInfo.profile().category().getName());

        return new TraceResult(progress, configurator.getCurrentTopic(interviewInfo.profile(), progress));
    }

}
