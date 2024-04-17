package com.mock.interview.interviewconversationpair.domain.model;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewconversationpair.domain.event.*;
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

    @OneToOne(fetch = FetchType.LAZY)
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

    private void waitQuestion() {
        if(status == PairStatus.COMPLETED)
            throw new IllegalStateException();

        status = PairStatus.WAITING_QUESTION;
    }

    public void startConversation() {
        if(status != PairStatus.START)
            throw new IllegalStateException();

        waitQuestion();
        Events.raise(new ConversationStartedEvent(interview.getId(), this.id));
    }

    // TODO: 수정필요. Question과 결합을 갖게 된다...
    public void restartConversationWithAi() {
        verifyRestartableStatus();
        waitQuestion();
        Events.raise(new AiQuestionRecommendedEvent(interview.getId(), this.id));
    }

    public void restartConversation() {
        verifyRestartableStatus();
        waitQuestion();
        Events.raise(new ConversationStartedEvent(interview.getId(), this.id));
    }

    public void connectQuestion(InterviewQuestion question) {
        if(status != PairStatus.WAITING_QUESTION)
            throw new IllegalStateException();

        this.question = question;
        status = PairStatus.WAITING_ANSWER;
        Events.raise(new QuestionConnectedEvent(interview.getId(), this.id, question.getId()));
    }

    public void reset(String resetMessage) {
        if(status != PairStatus.WAITING_QUESTION)
            throw new IllegalStateException("불필요한 reset");

        status = PairStatus.WAITING_ANSWER;
        Events.raise(new ConversationResetEvent(interview.getId(), this.id, resetMessage));
    }

    public void answerQuestion(InterviewAnswer answer) {
        verifyAnswerNeededStatus();

        this.answer = answer;
        this.status = PairStatus.COMPLETED;
        Events.raise(new ConversationCompletedEvent(interview.getId()));
    }

    public boolean isChangeTopicStatus() {
        return status == PairStatus.WAITING_QUESTION && question != null;
    }

    private void verifyAnswerNeededStatus() {
        if(isAnswerNeededStatus())
            throw new IllegalStateException("대답이 필요한 상태가 아닙니다.");
    }

    private void verifyRestartableStatus() {
        if(!isRestartableStatus())
            throw new IllegalStateException("재시작이 불가능한 상태입니다.");
    }

    private boolean isRestartableStatus() {
        return status == PairStatus.WAITING_ANSWER;
    }

    private boolean isAnswerNeededStatus() {
        return question != null && status == PairStatus.WAITING_ANSWER;
    }
}
