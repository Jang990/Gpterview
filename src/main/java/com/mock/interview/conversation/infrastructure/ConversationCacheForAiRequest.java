package com.mock.interview.conversation.infrastructure;

import com.mock.interview.conversation.application.ConversationConvertor;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.dto.MessageHistory;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationCacheForAiRequest {

    private final InterviewConversationPairRepository conversationPairRepository;

    public MessageHistory findCurrentConversation(long interviewId) {
        // TODO: 캐싱과 만료 방식 생각해볼 것.
        List<InterviewConversationPair> messageList  = conversationPairRepository
                .findCurrentConversationHistory(
                        interviewId,
                        PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "createdAt"))
                )
                .getContent()
                .stream()
                .sorted(Comparator.comparing(InterviewConversationPair::getCreatedAt))
                .toList();

        List<Message> history = new LinkedList<>();
        messageList.forEach(pair -> {
            history.add(ConversationConvertor.convert(pair.getQuestion()));
            history.add(ConversationConvertor.convert(pair.getAnswer()));
        });

        return new MessageHistory(history);
    }
}
