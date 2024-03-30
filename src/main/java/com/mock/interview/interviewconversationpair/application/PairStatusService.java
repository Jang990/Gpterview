package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interviewconversationpair.domain.PairAiStatusService;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PairStatusService {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final PairAiStatusService pairAiStatusService;

    public void changeQuestionTopic(long loginId, long interviewId, long pairId) {
        InterviewConversationPair conversationPair = conversationPairRepository.findWithInterviewUser(pairId, interviewId, loginId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        pairAiStatusService.changeTopic(conversationPair.getInterview(), conversationPair);
    }

    public void changeRequestingAi(long loginId, long interviewId, long pairId) {
        InterviewConversationPair conversationPair = conversationPairRepository.findWithInterviewUser(pairId, interviewId, loginId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        pairAiStatusService.changeRecommendationToAi(conversationPair.getInterview(), conversationPair);
    }
}
