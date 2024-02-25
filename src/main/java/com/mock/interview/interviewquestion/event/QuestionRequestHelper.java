package com.mock.interview.interviewquestion.event;

import com.mock.interview.conversation.infrastructure.ConversationCacheForAiRequest;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;

public class QuestionRequestHelper {
    static Message requestQuestion(
            AIService aiService, InterviewCacheForAiRequest interviewCache,
            ConversationCacheForAiRequest conversationCache, long interviewId
    ) {
        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
        MessageHistory messageHistory = conversationCache.findMessageHistory(interviewId);
        return aiService.service(interviewInfo, messageHistory);
    }
}
