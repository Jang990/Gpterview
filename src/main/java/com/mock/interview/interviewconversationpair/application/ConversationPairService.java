package com.mock.interview.interviewconversationpair.application;

import com.mock.interview.interview.infra.lock.progress.InterviewProgressLock;
import com.mock.interview.interview.infra.lock.progress.dto.InterviewConversationIds;
import com.mock.interview.interviewconversationpair.domain.AppearedQuestionIdManager;
import com.mock.interview.interviewconversationpair.domain.ConversationRestarter;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import com.mock.interview.interviewquestion.domain.exception.InterviewQuestionNotFoundException;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.interviewquestion.infra.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationPairService {
    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewQuestionRepository questionRepository;
    private final ConversationRestarter conversationRestarter;
    private final AppearedQuestionIdManager appearedQuestionIdManager;

    @InterviewProgressLock
    public void connectQuestion(InterviewConversationIds lockDto, long questionId) {
        InterviewConversationPair conversationPair = conversationPairRepository
                .findWithInterviewUser(lockDto.conversationId(), lockDto.interviewId(), lockDto.userId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        InterviewQuestion question = questionRepository.findOpenQuestion(questionId)
                .orElseThrow(InterviewQuestionNotFoundException::new);

        conversationPair.connectQuestion(question, appearedQuestionIdManager);
    }

    @InterviewProgressLock
    public void recommendAnotherQuestion(InterviewConversationIds lockDto) {
        InterviewConversationPair conversationPair = conversationPairRepository
                .findWithInterviewUser(lockDto.conversationId(), lockDto.interviewId(), lockDto.userId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        conversationRestarter.restart(conversationPair.getInterview(), conversationPair);
    }

    @InterviewProgressLock
    public void recommendAiQuestion(InterviewConversationIds conversationDto) {
        InterviewConversationPair conversationPair = conversationPairRepository
                .findWithInterviewUser(conversationDto.conversationId(), conversationDto.interviewId(), conversationDto.userId())
                .orElseThrow(InterviewConversationPairNotFoundException::new);
        conversationRestarter.restartOnlyAi(conversationPair.getInterview(), conversationPair);
    }
}
