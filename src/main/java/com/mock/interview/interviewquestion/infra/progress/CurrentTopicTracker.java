package com.mock.interview.interviewquestion.infra.progress;

import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgress;
import com.mock.interview.interviewquestion.infra.ai.progress.InterviewProgressTimeBasedTracker;
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

    public String trace(InterviewInfo interviewInfo) {
        InterviewProgress progress = progressTracker.getCurrentInterviewProgress(interviewInfo.config());
        InterviewPromptConfigurator configurator = InterviewPromptConfigurator
                .selectPromptConfigurator(interviewPromptConfiguratorList, interviewInfo);

        return configurator.getCurrentTopic(interviewInfo.profile(), progress);
    }

}
