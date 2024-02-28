package com.mock.interview.interviewanswer.application;

import com.mock.interview.conversation.domain.model.ConversationCreationService;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.interview.application.InterviewVerificationHelper;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.exception.InterviewConversationPairNotFoundException;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewAnswerInInterviewService {
    private final InterviewRepository interviewRepository;
    private final InterviewConversationRepository conversationRepository;
    private final ConversationCreationService conversationCreationService;

    private final InterviewConversationPairRepository conversationPairRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    public void create(long loginId, long interviewId, long questionId, MessageDto answerDto) {
        InterviewVerificationHelper.verify(interviewRepository, interviewId, loginId);
        InterviewConversationPair conversationPair = conversationPairRepository.findConversation(interviewId, questionId)
                .orElseThrow(InterviewConversationPairNotFoundException::new);

        // TODO: 이 부분은 이벤트나 도메인으로 뺄 생각을 하면 좋을 것 같다.
        InterviewAnswer answer = InterviewAnswer
                .createAnswer(conversationPair.getQuestion(), answerDto.getContent());
        interviewAnswerRepository.save(answer);

        conversationPair.answerQuestion(answer);
    }

    public void changeQuestionTopic(long loginId, long interviewId, long questionId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        Optional<InterviewConversation> lastConversation = conversationRepository.findLastConversation(interview.getId());
        conversationCreationService.changeTopic(interview, lastConversation);
    }
}
