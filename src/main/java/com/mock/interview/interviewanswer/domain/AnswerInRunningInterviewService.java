package com.mock.interview.interviewanswer.domain;

import com.mock.interview.conversation.domain.UserAnsweredEvent;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.global.Events;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewanswer.infra.InterviewAnswerRepository;
import com.mock.interview.interviewconversationpair.domain.model.InterviewConversationPair;
import org.springframework.stereotype.Service;

@Service
public class AnswerInRunningInterviewService {
    public void saveAnswerInInterview(
            InterviewAnswerRepository interviewAnswerRepository,
            Interview interview, InterviewConversationPair conversationPair,
            MessageDto answerDto
    ) {
        InterviewAnswer answer = InterviewAnswer.createAnswer(conversationPair.getQuestion(), answerDto.getContent());
        interviewAnswerRepository.save(answer);

        conversationPair.answerQuestion(answer);

        if(interview.isActive())
            Events.raise(new UserAnsweredEvent(interview.getId()));
    }
}
