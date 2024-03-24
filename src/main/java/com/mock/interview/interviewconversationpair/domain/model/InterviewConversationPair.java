package com.mock.interview.interviewconversationpair.domain.model;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewconversationpair.domain.ConversationCompletedEvent;
import com.mock.interview.interviewconversationpair.domain.AiQuestionRecommendedEvent;
import com.mock.interview.interviewconversationpair.domain.QuestionConnectedEvent;
import com.mock.interview.interviewconversationpair.domain.StatusChangedToChangingEvent;
import com.mock.interview.interviewconversationpair.domain.exception.IsAlreadyCompletedConversationException;
import com.mock.interview.interviewconversationpair.domain.exception.IsAlreadyChangingStateException;
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
    @JoinColumn(name = "question_id")
    private InterviewQuestion question;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private InterviewAnswer answer;

    @Enumerated(EnumType.STRING)
    private PairStatus status;

    public static InterviewConversationPair create(Interview interview) {
        InterviewConversationPair conversationPair = new InterviewConversationPair();
        conversationPair.interview = interview;
        conversationPair.status = PairStatus.START;
        return conversationPair;
    }

    public void recommendAiQuestion() {
        verifyCanConnectQuestion();
        Events.raise(new AiQuestionRecommendedEvent(interview.getId(), this.id));
    }

    public void connectQuestion(InterviewQuestion question) {
        verifyCanConnectQuestion();

        this.question = question;
        status = PairStatus.WAITING_ANSWER;
        Events.raise(new QuestionConnectedEvent(interview.getId(), this.getId(), question.getId()));
    }

    public void answerQuestion(InterviewAnswer answer) {
        verifyCanModifyQuestion();

        this.answer = answer;
        this.status = PairStatus.COMPLETED;
        Events.raise(new ConversationCompletedEvent(interview.getId()));
    }

    // TODO: 디폴트 접근 제어자로 수정할 것
    public void changeStatusToChangeTopic() {
        verifyCanModifyQuestion();
        status = PairStatus.CHANGING;
        Events.raise(new StatusChangedToChangingEvent(interview.getId(), this.getId()));
    }

    public void changeTopic(InterviewQuestion question) {
        if(status != PairStatus.CHANGING)
            throw new IllegalStateException();

        this.question = question;
        this.status = PairStatus.WAITING_ANSWER;
        Events.raise(new QuestionConnectedEvent(interview.getId(), id, question.getId()));
    }

    private void verifyCanConnectQuestion() {
        if(status != PairStatus.START)
            throw new IllegalStateException();
    }

    private void verifyCanModifyQuestion() {
        if(status == PairStatus.COMPLETED)
            throw new IsAlreadyCompletedConversationException();
        if(status == PairStatus.CHANGING)
            throw new IsAlreadyChangingStateException();
    }
}
