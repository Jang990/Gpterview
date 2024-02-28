package com.mock.interview.interviewconversationpair.domain.model;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewconversationpair.domain.PairStatusChangedToChangingEvent;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewConversationPair extends BaseTimeEntity {
    @Id
    @Column(name = "pair_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion question;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private InterviewAnswer answer;

    @Enumerated(EnumType.STRING)
    private PairStatus status;

    public static InterviewConversationPair startConversation(Interview interview, InterviewQuestion question) {
        InterviewConversationPair conversationPair = new InterviewConversationPair();
        conversationPair.interview = interview;
        conversationPair.question = question;
        conversationPair.status = PairStatus.WAITING;
        return conversationPair;
    }

    public void answerQuestion(InterviewAnswer answer) {
        if(cannotAnswer())
            throw new IllegalStateException(); // TODO: 커스텀 예외 필요

        this.answer = answer;
        this.status = PairStatus.COMPLETED;
    }

    public boolean cannotAnswer() {
        return status == PairStatus.COMPLETED || status == PairStatus.CHANGING;
    }

    public void changeTopic() {
        status = PairStatus.CHANGING;
        Events.raise(new PairStatusChangedToChangingEvent(id));
    }
}
