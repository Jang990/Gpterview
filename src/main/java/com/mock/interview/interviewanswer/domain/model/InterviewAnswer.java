package com.mock.interview.interviewanswer.domain.model;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseEntity;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.user.domain.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewAnswer extends BaseTimeEntity {
    @Id
    @Column(name = "interview_answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private InterviewQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    private long likes;

    public static InterviewAnswer createAnswer(InterviewQuestion question, String answer, Users users) {
        InterviewAnswer interviewAnswer = new InterviewAnswer();
        interviewAnswer.question = question;
        interviewAnswer.answer = answer;
        interviewAnswer.users = users;
        interviewAnswer.likes = 0L;
        return interviewAnswer;
    }
}
