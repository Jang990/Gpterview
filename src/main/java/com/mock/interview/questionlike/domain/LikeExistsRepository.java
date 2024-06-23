package com.mock.interview.questionlike.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mock.interview.questionlike.domain.QQuestionLike.questionLike;

@Repository
@RequiredArgsConstructor
public class LikeExistsRepository {
    private final JPAQueryFactory query;

    public boolean isExist(long questionId, long userId) {
        Integer exist = query.selectOne().
                from(questionLike)
                .where(questionLike.question.id.eq(questionId), questionLike.users.id.eq(userId))
                .fetchFirst();

        return exist != null;
    }
}
