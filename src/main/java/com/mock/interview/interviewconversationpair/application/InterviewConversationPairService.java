package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.conversation.domain.model.ConversationCreationService;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewConversationPairService {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationRepository conversationRepository;
    private final ConversationCreationService conversationCreationService;

    public void saveUserAnswer(Long loginId, long interviewId, MessageDto message) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        Optional<InterviewConversation> lastConversation = conversationRepository.findLastConversation(interview.getId());
        InterviewConversation conversation = conversationCreationService.createAnswer(interview, message, lastConversation);
        conversationRepository.save(conversation);
    }

    public void changeQuestionTopic(long loginId, long interviewId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        Optional<InterviewConversation> lastConversation = conversationRepository.findLastConversation(interview.getId());
        conversationCreationService.changeTopic(interview, lastConversation);
    }
}
