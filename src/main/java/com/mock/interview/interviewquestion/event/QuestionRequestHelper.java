package com.mock.interview.interviewquestion.event;

import com.mock.interview.interviewconversationpair.infra.ConversationCacheForAiRequest;
import com.mock.interview.interviewquestion.infra.interview.AIService;
import com.mock.interview.interviewquestion.infra.interview.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.interview.dto.Message;
import com.mock.interview.interviewquestion.infra.interview.dto.MessageHistory;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;

public class QuestionRequestHelper {
    public static Message requestQuestion(
            AIService aiService, InterviewCacheForAiRequest interviewCache,
            ConversationCacheForAiRequest conversationCache, long interviewId
    ) {
        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
        MessageHistory messageHistory = conversationCache.findCurrentConversation(interviewId);
        return aiService.service(interviewInfo, messageHistory);
    }
}
