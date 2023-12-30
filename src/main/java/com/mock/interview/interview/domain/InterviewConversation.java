package com.mock.interview.interview.domain;

import com.mock.interview.global.auditing.BaseTimeEntity;
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

    private String topic;

    @Enumerated(EnumType.STRING)
    private InterviewConversationType interviewConversationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", updatable = false)
    private Interview interview;
}
