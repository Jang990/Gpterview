package com.mock.interview.interviewconversationpair.domain.model;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewconversationpair.domain.*;
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
        if(status != PairStatus.START && status != PairStatus.RECOMMENDING)
            throw new IllegalStateException();
        status = PairStatus.WAITING_AI;
        Events.raise(new AiQuestionRecommendedEvent(interview.getId(), this.id));
    }

    public void recommendExistingQuestion() {
        if(status != PairStatus.START)
            throw new IllegalStateException();

        status = PairStatus.RECOMMENDING;
        Events.raise(new ExistingQuestionRecommendedEvent(interview.getId(), this.id));
    }

    public void recommendAnotherQuestions() {
        if(status != PairStatus.RECOMMENDING)
            throw new IllegalStateException();

        Events.raise(new AnotherQuestionRecommendedEvent(interview.getId(), this.id));
    }


    public void connectAiQuestion(InterviewQuestion question) {
        if(status != PairStatus.WAITING_AI)
            throw new IllegalStateException();

        this.question = question;
        status = PairStatus.WAITING_ANSWER;
        Events.raise(new QuestionConnectedEvent(interview.getId(), this.getId(), question.getId()));
    }

    public void connectQuestion(InterviewQuestion question) {
        if(status != PairStatus.RECOMMENDING)
            throw new IllegalStateException();

        this.question = question;
        status = PairStatus.WAITING_ANSWER;
    }

    public void answerQuestion(InterviewAnswer answer) {
        verifyWaitingAnswer();

        this.answer = answer;
        this.status = PairStatus.COMPLETED;
        Events.raise(new ConversationCompletedEvent(interview.getId()));
    }

    public void changeTopic(InterviewQuestion question) {
        if(status != PairStatus.CHANGING)
            throw new IllegalStateException();

        this.question = question;
        this.status = PairStatus.WAITING_ANSWER;
        Events.raise(new QuestionConnectedEvent(interview.getId(), id, question.getId()));
    }

    // TODO: 디폴트 접근 제어자로 수정할 것
    public void changeStatusToChangeTopic() {
        verifyWaitingAnswer();
        status = PairStatus.CHANGING;
        Events.raise(new StatusChangedToChangingEvent(interview.getId(), this.getId()));
    }

    public boolean isRecommendationDeniedState() {
        return status != PairStatus.RECOMMENDING;
    }

    private void verifyWaitingAnswer() {
        if(status != PairStatus.WAITING_ANSWER)
            throw new IllegalStateException();
    }
}
