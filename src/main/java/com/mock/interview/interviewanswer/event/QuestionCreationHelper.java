package com.mock.interview.interviewanswer.event;

import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.ConversationCacheForAiRequest;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.InterviewInfo;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.infrastructure.InterviewCacheForAiRequest;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;

public class QuestionCreationHelper {
//    static void saveAiMessage(
//            InterviewQuestionRepository repository,
//            ConversationMessageBroker conversationMessageBroker,
//            InterviewConversationPair conversationPair, Message message
//    ) {
//        InterviewQuestion.createInInterview()
//        InterviewConversation interviewConversation = InterviewConversation.createQuestion(conversationPair, message);
//
//        conversationMessageBroker.publish(conversationPair.getId(), new MessageDto(interviewConversation.getId(), message.getRole(), message.getContent()));
//    }
//
//    static Message getInterviewQuestion(AIService aiService, InterviewCacheForAiRequest interviewCache, ConversationCacheForAiRequest conversationCache, long interviewId) {
//        InterviewInfo interviewInfo = interviewCache.findAiInterviewSetting(interviewId);
//        MessageHistory messageHistory = conversationCache.findMessageHistory(interviewId);
//        return aiService.changeTopic(interviewInfo, messageHistory);
//    }
}
