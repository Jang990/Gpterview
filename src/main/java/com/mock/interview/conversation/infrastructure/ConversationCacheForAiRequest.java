package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.application.ConversationConvertor;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationCacheForAiRequest {

    private final InterviewConversationRepository conversationRepository;

    public MessageHistory findMessageHistory(long interviewId) {
        // TODO: 캐싱과 만료 방식 생각해볼 것.
        List<Message> messageList  = conversationRepository
                .findByInterviewId(
                        interviewId,
                        PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "createdAt"))
                )
                .getContent()
                .stream()
                .sorted(Comparator.comparing(InterviewConversation::getCreatedAt))
                .map(ConversationConvertor::convert)
                .toList();

        return new MessageHistory(messageList);
    }
}
