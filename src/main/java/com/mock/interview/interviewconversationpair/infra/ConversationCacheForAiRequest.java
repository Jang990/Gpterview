package com.mock.interview.interviewconversationpair.infra;

import com.mock.interview.interview.presentation.dto.InterviewRole;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewquestion.infra.ai.dto.Message;
import com.mock.interview.interviewquestion.infra.ai.dto.MessageHistory;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
            history.add(convert(pair.getQuestion()));
            Message answer = convert(pair.getAnswer());
            if(answer != null)
                history.add(answer);
        });

        return new MessageHistory(history);
    }

    public static Message convert(InterviewQuestion interviewQuestion) {
        return new Message(InterviewRole.AI.toString(), interviewQuestion.getQuestion());
    }

    public static Message convert(InterviewAnswer interviewAnswer) {
        if(interviewAnswer == null)
            return null;
        return new Message(InterviewRole.USER.toString(), interviewAnswer.getAnswer());
    }
}
