package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewconversationpair.application.LastConversationHelper;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.infra.ai.dto.InterviewInfo;
import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentConversation;
import com.mock.interview.questiontoken.domain.KoreaStringAnalyzer;

import java.util.Optional;

public final class CurrentConversationConvertor {
    private CurrentConversationConvertor() {}

    public static CurrentConversation create(
            InterviewConversationPairRepository conversationPairRepository,
            KoreaStringAnalyzer stringAnalyzer,
            long interviewId, InterviewInfo interview, String currentTopic
    ) {
        Optional<InterviewConversationPair> optCurrentConversation = LastConversationHelper
                .findCurrentCompletedConversation(conversationPairRepository, interviewId);

        if(optCurrentConversation.isEmpty())
            return createEmpty(interview, currentTopic);

        InterviewConversationPair currentConversation = optCurrentConversation.get();
        if(currentConversation.getAnswer() == null)
            return createEmpty(interview, currentTopic);

        return new CurrentConversation(
                currentConversation.getAnswer().getId(),
                stringAnalyzer.extractNecessaryTokens(currentConversation.getAnswer().getAnswer()),
                currentTopic, interview.profile().field()
        );
    }

    private static CurrentConversation createEmpty(InterviewInfo interview, String topic) {
        return new CurrentConversation(null,null, topic, interview.profile().field());
    }
}
