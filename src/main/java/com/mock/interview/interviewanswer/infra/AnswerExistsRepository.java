package com.mock.interview.interviewanswer.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;

@Repository
@RequiredArgsConstructor
public class AnswerExistsRepository {

    private final JPAQueryFactory query;

    public boolean isExist(long answerId, long questionId, long userId) {
        Integer exist = query.selectOne().
                from(interviewAnswer)
                .where(
                        interviewAnswer.id.eq(answerId),
                        interviewAnswer.question.id.eq(questionId),
                        interviewAnswer.users.id.eq(userId)
                )
                .fetchFirst();

        return exist != null;
    }
}
