package com.mock.interview.conversation.domain.model;

import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.Interview;
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

    @Column(updatable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private InterviewConversationType interviewConversationType;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", updatable = false)
    private Interview interview;

    public static InterviewConversation createAnswer(Interview interview, MessageDto answer) {
        if (answer.getRole() == null || answer.getContent() == null
                || !answer.getRole().equals("INTERVIEWER")) {
            throw new IllegalArgumentException();
        }

        InterviewConversation conversation = new InterviewConversation();
        conversation.interviewConversationType = InterviewConversationType.QUESTION;
        conversation.content = answer.getContent();
        conversation.interview = interview;
        conversation.isDeleted = false;
        return conversation;
    }

    public static InterviewConversation createQuestion(Interview interview, MessageDto question) {
        if (question.getRole() == null || question.getContent() == null
                || !question.getRole().equals("INTERVIEWER")) {
            throw new IllegalArgumentException();
        }

        InterviewConversation conversation = new InterviewConversation();
        conversation.interviewConversationType = InterviewConversationType.ANSWER;
        conversation.content = question.getContent();
        conversation.interview = interview;
        conversation.isDeleted = false;
        return conversation;
    }
}
