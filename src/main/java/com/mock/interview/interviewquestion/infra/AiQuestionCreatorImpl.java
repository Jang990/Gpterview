package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interview.infra.cache.dto.InterviewInfo;
import com.mock.interview.interview.infra.progress.dto.InterviewProgress;
import com.mock.interview.interviewquestion.infra.gpt.prompt.InterviewPromptCreationService;
import com.mock.interview.interviewquestion.domain.AiQuestionCreator;
import com.mock.interview.interviewquestion.infra.gpt.ChatGPTRequester;
import com.mock.interview.interviewquestion.infra.gpt.dto.MessageHistory;
import com.mock.interview.interviewquestion.infra.gpt.dto.InterviewAIRequest;
import com.mock.interview.interviewquestion.infra.gpt.prompt.AiPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiQuestionCreatorImpl implements AiQuestionCreator {
    private final ChatGPTRequester requester;
    private final InterviewPromptCreationService promptCreationService;

    @Override
    public String create(
            InterviewInfo interviewInfo,
            InterviewProgress interviewProgress,
            MessageHistory history,
            CreationOption creationOption
    ) {
        AiPrompt prompt = promptCreationService.create(interviewInfo, interviewProgress, creationOption);
        // TODO: AI에 request 토큰 제한이 있기 때문에 message List에서 필요한 부분만 추출해서 넣어야 함.
        return requester.sendRequest(new InterviewAIRequest(history.getMessages(), prompt));
    }
}
