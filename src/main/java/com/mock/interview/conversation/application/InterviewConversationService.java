package com.mock.interview.conversation.application;

import com.mock.interview.conversation.domain.model.ConversationCreationService;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.domain.model.InterviewConversationType;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.conversation.presentation.dto.InterviewRole;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewConversationService {

    private final InterviewRepository interviewRepository;
    private final InterviewConversationRepository conversationRepository;
    private final ConversationCreationService conversationCreationService;

    private final int FIRST_PAGE = 0;
    private final int CONVERSATION_OFFSET = 20;

    // TODO: saveAnswer와 saveQuestion을 합치고 AIService를 DIP를 통해 도메인 영역으로 올려야함.
    public void saveUserAnswer(Long loginId, long interviewId, MessageDto message) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        Optional<InterviewConversation> lastConversation = conversationRepository.findLastConversation(interview.getId());
        InterviewConversation conversation = conversationCreationService.createAnswer(interview, message, lastConversation);
        conversationRepository.save(conversation);
    }

    @Transactional(readOnly = true)
    public MessageHistory findConversationsForAIRequest(long interviewId) {
        return convert(conversationRepository.findConversation(interviewId, PageRequest.of(FIRST_PAGE, CONVERSATION_OFFSET)));
    }

    private MessageHistory convert(Page<InterviewConversation> conversation) {
        List<Message> list = new LinkedList<>();
        for (InterviewConversation msg : conversation.getContent()) {
            list.add(0, new Message(getRole(msg),msg.getContent()));
        }
        return new MessageHistory(list);
    }

    private static String getRole(InterviewConversation msg) {
        return msg.getInterviewConversationType() == InterviewConversationType.ANSWER
                ? InterviewRole.INTERVIEWER.toString() : InterviewRole.USER.toString();
    }
}
