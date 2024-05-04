package com.mock.interview.questionlike.domain;

import com.mock.interview.global.Events;
import com.mock.interview.global.auditing.BaseTimeEntity;
import com.mock.interview.interviewquestion.domain.model.InterviewQuestion;
import com.mock.interview.questionlike.domain.event.QuestionLikeCanceledEvent;
import com.mock.interview.questionlike.domain.event.QuestionLikedEvent;
import com.mock.interview.user.domain.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionLike extends BaseTimeEntity {
    @Id
    @Column(name = "question_likes_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_user", nullable = false)
    private Users users;

    public static QuestionLike likeQuestion(QuestionLikesRepository repository, Users users, InterviewQuestion question) {
        QuestionLike questionLike = new QuestionLike();
        questionLike.users = users;
        questionLike.question = question;

        repository.save(questionLike);
        Events.raise(new QuestionLikedEvent(question.getId()));
        return questionLike;
    }

    public void delete() {
        Events.raise(new QuestionLikeCanceledEvent(question.getId()));
    }
}
