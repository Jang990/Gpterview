package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewquestion.infra.RecommendedQuestion;
import com.mock.interview.interviewquestion.infra.ai.AiQuestionCreator;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.ai.dto.MessageHistory;
import com.mock.interview.interview.infra.InterviewCacheForAiRequest;

public class AiQuestionHelper {
    public static RecommendedQuestion createQuestion(
            AiQuestionCreator aiQuestionCreator, InterviewCacheForAiRequest interviewCache,
            ConversationCacheForAiRequest conversationCache, long interviewId
    ) {
        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
        MessageHistory messageHistory = conversationCache.findCurrentConversation(interviewId);
        return aiQuestionCreator.service(interviewInfo, messageHistory);
    }

    public static RecommendedQuestion changeTopic(
            AiQuestionCreator aiQuestionCreator, InterviewCacheForAiRequest interviewCache,
            ConversationCacheForAiRequest conversationCache, long interviewId
    ) {
        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
        MessageHistory messageHistory = conversationCache.findCurrentConversation(interviewId);
        return aiQuestionCreator.changeTopic(interviewInfo, messageHistory);
    }
}
