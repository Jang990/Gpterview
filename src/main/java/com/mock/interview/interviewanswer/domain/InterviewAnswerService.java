package com.mock.interview.interviewanswer.domain;

import com.mock.interview.interviewanswer.presentation.dto.InterviewAnswerRequest;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.event.ConversationAnsweredEvent;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.stereotype.Service;

@Service
public class InterviewAnswerService {
    public void saveAnswerInInterview(
            InterviewAnswerRepository interviewAnswerRepository,
            Interview interview, InterviewConversationPair conversationPair,
            InterviewAnswerRequest answerDto
    ) {
        InterviewAnswer answer = InterviewAnswer.createAnswer(conversationPair.getQuestion(), answerDto.getContent(), interview.getUsers());
        interviewAnswerRepository.save(answer);
        Events.raise(new ConversationAnsweredEvent(conversationPair.getId(), answer.getId()));
    }
}
