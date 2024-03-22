package com.mock.interview.interviewconversationpair.domain.model;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewconversationpair.domain.NewQuestionAddedEvent;
import com.mock.interview.interviewconversationpair.domain.exception.IsAlreadyAnsweredConversationException;
import com.mock.interview.interviewconversationpair.domain.exception.IsAlreadyChangingStateException;
import com.mock.interview.interviewconversationpair.infra.InterviewConversationPairRepository;
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

    public static InterviewConversationPair startConversation(
            InterviewConversationPairRepository conversationPairRepository,
            Interview interview, InterviewQuestion question
    ) {
        InterviewConversationPair conversationPair = new InterviewConversationPair();
        conversationPair.interview = interview;
        conversationPair.question = question;
        conversationPair.status = PairStatus.WAITING;

        conversationPairRepository.save(conversationPair);
        Events.raise(new NewQuestionAddedEvent(interview.getId(), conversationPair.id, question.getId()));
        return conversationPair;
    }

    public void answerQuestion(InterviewAnswer answer) {
        verifyCanModifyQuestion();

        this.answer = answer;
        this.status = PairStatus.COMPLETED;
    }

    // TODO: 디폴트 접근 제어자로 수정할 것
    public void changeStatusToChangeTopic() {
        verifyCanModifyQuestion();
        status = PairStatus.CHANGING;
    }

    public void changeTopic(InterviewQuestion question) {
        if(status != PairStatus.CHANGING)
            throw new IllegalStateException(); // TODO: 적절한 예외 필요.

        this.question = question;
        this.status = PairStatus.WAITING;

        Events.raise(new NewQuestionAddedEvent(interview.getId(), id, question.getId()));
    }

    private void verifyCanModifyQuestion() {
        if( status == PairStatus.COMPLETED)
            throw new IsAlreadyAnsweredConversationException();
        if(status == PairStatus.CHANGING)
            throw new IsAlreadyChangingStateException();
    }
}
