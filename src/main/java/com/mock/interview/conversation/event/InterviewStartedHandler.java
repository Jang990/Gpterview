package com.mock.interview.conversation.event;

import com.mock.interview.candidate.domain.model.CandidateConfig;
import com.mock.interview.conversation.domain.ConversationMessageBroker;
import com.mock.interview.conversation.domain.model.InterviewConversation;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.infrastructure.interview.AIService;
import com.mock.interview.conversation.infrastructure.interview.dto.*;
import com.mock.interview.interview.domain.InterviewStartedEvent;
import com.mock.interview.interview.infrastructure.InterviewRepository;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewStartedHandler {
    private final AIService aiService;
    private final InterviewConversationRepository interviewConversationRepository;
    private final InterviewRepository interviewRepository;

//    @EventListener(InterviewStartedEvent.class)
    public void createQuestion(InterviewStartedEvent event) {
//        InterviewInfo interviewInfo = convert(event.config(), event.interviewExpiredTime());
//        Message question = aiService.getQuestion(interviewInfo, new MessageHistory(new LinkedList<>()));
//        InterviewConversation conversation = InterviewConversation.createQuestion();
//        messageBroker.publish(event.user().getId(), question);
    }

    private InterviewInfo convert(CandidateConfig config, LocalDateTime interviewExpiredTime) {
        InterviewProfile profile = new InterviewProfile(
                config.getDepartment().getName(),
                config.getAppliedJob().getName(),
                config.getTechSubjects().stream().map(TechnicalSubjects::getName).toList(),
                List.of(config.getExperience())
        );
        InterviewConfig interviewConfig = new InterviewConfig(config.getType(), interviewExpiredTime);
        return new InterviewInfo(profile, interviewConfig);
    }


}
