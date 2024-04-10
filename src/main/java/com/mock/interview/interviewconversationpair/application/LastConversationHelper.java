package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public final class LastConversationHelper {
    private static final Pageable LIMIT_ONE = PageRequest.of(0, 1);

    private LastConversationHelper() {}

    public static InterviewConversationPair findLastConversation(InterviewConversationPairRepository repository, long interviewId) {
        List<InterviewConversationPair> lastCompletedConversation = repository.findLastCompletedConversation(interviewId, LIMIT_ONE);
        if(lastCompletedConversation.isEmpty())
            throw new IllegalArgumentException();
        return lastCompletedConversation.get(0);
    }

    public static InterviewConversationPair findLastConversation(InterviewConversationPairRepository repository, long userId, long interviewId) {
        List<InterviewConversationPair> lastCompletedConversation = repository.findLastCompletedConversation(userId, interviewId, LIMIT_ONE);
        if(lastCompletedConversation.isEmpty())
            throw new IllegalArgumentException();
        return lastCompletedConversation.get(0);
    }
}
