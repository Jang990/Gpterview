package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interview.infra.lock.progress.InterviewProgressLock;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationLockDto;
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

    @InterviewProgressLock
    public void changeQuestionTopic(InterviewConversationLockDto conversationDto) {
        InterviewConversationPair conversationPair = conversationPairRepository
                .findWithInterviewUser(conversationDto.conversationId(), conversationDto.interviewId(), conversationDto.userId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        pairAiStatusService.changeTopic(conversationPair.getInterview(), conversationPair);
    }

    @InterviewProgressLock
    public void changeRequestingAi(InterviewConversationLockDto conversationDto) {
        InterviewConversationPair conversationPair = conversationPairRepository
                .findWithInterviewUser(conversationDto.conversationId(), conversationDto.interviewId(), conversationDto.userId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        pairAiStatusService.changeRecommendationToAi(conversationPair.getInterview(), conversationPair);
    }
}
