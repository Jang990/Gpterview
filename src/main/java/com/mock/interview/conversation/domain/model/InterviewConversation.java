package com.mock.interview.conversation.domain.model;

import com.mock.interview.conversation.domain.AiAnsweredEvent;
import com.mock.interview.conversation.domain.ChangeTopicEvent;
import com.mock.interview.conversation.domain.exception.CannotChangeUserTopicException;
import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.conversation.presentation.dto.MessageDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewConversation extends BaseTimeEntity {
    @Id
    @Column(name = "conversation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, length = 600) // 한글 200자 제한
    private String content;

    @Enumerated(EnumType.STRING)
    private InterviewConversationType interviewConversationType;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", updatable = false)
    private Interview interview;

    static InterviewConversation createAnswer(Interview interview, MessageDto answer) {
        if (answer.getRole() == null || answer.getContent() == null
                || !answer.getRole().equals(InterviewConversationType.USER.toString())) {
            throw new IllegalArgumentException();
        }

        InterviewConversation conversation = new InterviewConversation();
        conversation.interviewConversationType = InterviewConversationType.USER;
        conversation.content = answer.getContent();
        conversation.interview = interview;
        conversation.isDeleted = false;
        return conversation;
    }

    public static InterviewConversation createQuestion(Interview interview, Message question) {
        if (question.getRole() == null || question.getContent() == null
                || !question.getRole().equals(InterviewConversationType.AI.toString())) {
            throw new IllegalArgumentException();
        }

        InterviewConversation conversation = new InterviewConversation();
        conversation.interviewConversationType = InterviewConversationType.AI;
        conversation.content = question.getContent();
        conversation.interview = interview;
        conversation.isDeleted = false;
        return conversation;
    }

    public void changeTopic() {
        if(isUserAnswer())
            throw new CannotChangeUserTopicException();

        isDeleted = true;
        Events.raise(new ChangeTopicEvent(interview.getId()));
    }

    boolean isUserAnswer() {
        return interviewConversationType == InterviewConversationType.USER;
    }
}
