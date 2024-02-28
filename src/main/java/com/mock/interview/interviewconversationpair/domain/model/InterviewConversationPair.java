package com.mock.interview.interviewconversationpair.domain.model;

import com.mock.interview.conversation.presentation.dto.MessageDto;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interview.domain.model.Interview;
import com.mock.interview.interviewanswer.domain.model.InterviewAnswer;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    public static InterviewConversationPair startConversation(Interview interview, InterviewQuestion question) {
        InterviewConversationPair conversationPair = new InterviewConversationPair();
        conversationPair.interview = interview;
        conversationPair.question = question;
        return conversationPair;
    }

    public void answerQuestion(InterviewAnswer answer) {
        if(answer != null)
            throw new IllegalArgumentException();

        this.answer = answer;
    }
}
