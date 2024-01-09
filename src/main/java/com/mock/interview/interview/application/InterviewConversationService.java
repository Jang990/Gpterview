package com.mock.interview.interview.application;

import com.mock.interview.interview.InterviewDomain;
import com.mock.interview.interview.domain.Interview;
import com.mock.interview.interview.domain.InterviewConversation;
import com.mock.interview.interview.domain.InterviewConversationType;
import com.mock.interview.interview.domain.exception.InterviewNotFoundException;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.infrastructure.InterviewConversationRepository;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.interview.infrastructure.interview.dto.Message;
import com.mock.interview.interview.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interview.presentation.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewConversationService {

    private final InterviewRepository interviewRepository;
    private final InterviewConversationRepository conversationRepository;
    private final InterviewDomain interviewDomain;

    private final int FIRST_PAGE = 0;
    private final int CONVERSATION_OFFSET = 20;

    @Transactional(noRollbackFor = IsAlreadyTimeoutInterviewException.class)
    public void saveAnswer(Long loginId, long interviewId, MessageDto message) {
        // TODO: 추후 타임아웃 로직 삭제하고, Redis같은 캐시로 타임아웃 관리 + timeout 처리
        Interview interview = getActiveInterview(loginId, interviewId);
        InterviewConversation conversation = InterviewConversation.createAnswer(interview, message);
        conversationRepository.save(conversation);
    }

    public void saveQuestion(Long loginId, long interviewId, MessageDto message) {
        Interview interview = interviewRepository.findByIdWitUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        InterviewConversation conversation = InterviewConversation.createQuestion(interview, message);
        conversationRepository.save(conversation);
    }

    public Interview getActiveInterview(Long loginId, long interviewId) {
        Interview interview = interviewRepository.findByIdWitUserId(interviewId, loginId)
                .orElseThrow(InterviewNotFoundException::new);
        interviewDomain.verifyTime(interview);
        return interview;
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
