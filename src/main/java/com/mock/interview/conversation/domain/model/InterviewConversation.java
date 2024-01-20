package com.mock.interview.conversation.domain.model;

import com.mock.interview.conversation.domain.AiAnsweredEvent;
import com.mock.interview.conversation.domain.UserAnsweredEvent;
import com.mock.interview.conversation.infrastructure.InterviewConversationRepository;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.exception.IsAlreadyTimeoutInterviewException;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewConversation extends BaseTimeEntity {
    @Id
    @Column(name = "conversation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private InterviewConversationType interviewConversationType;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", updatable = false)
    private Interview interview;

    public static InterviewConversation createAnswer(
            Interview interview, MessageDto answer,
            Optional<InterviewConversation> lastConversation
    ) {
        System.out.println(answer);
        if (answer.getRole() == null || answer.getContent() == null
                || !answer.getRole().equals("USER")) {
            throw new IllegalArgumentException();
        }

        if (interview.isTimeout()
                && (lastConversation.isEmpty() || lastConversation.get().isUserAnswer())) {
            throw new IsAlreadyTimeoutInterviewException();
        }

        InterviewConversation conversation = new InterviewConversation();
        conversation.interviewConversationType = InterviewConversationType.QUESTION;
        conversation.content = answer.getContent();
        conversation.interview = interview;
        conversation.isDeleted = false;

        if (!interview.isTimeout()) {
            Events.raise(new UserAnsweredEvent(interview.getId()));
        }
        return conversation;
    }

    public static InterviewConversation createQuestion(Interview interview, Message question) {
        if (question.getRole() == null || question.getContent() == null
                || !question.getRole().equals("INTERVIEWER")) {
            throw new IllegalArgumentException();
        }

        InterviewConversation conversation = new InterviewConversation();
        conversation.interviewConversationType = InterviewConversationType.ANSWER;
        conversation.content = question.getContent();
        conversation.interview = interview;
        conversation.isDeleted = false;

        Events.raise(new AiAnsweredEvent(interview.getId(), question));
        return conversation;
    }

    boolean isUserAnswer() {
        return interviewConversationType == InterviewConversationType.ANSWER;
    }
}
