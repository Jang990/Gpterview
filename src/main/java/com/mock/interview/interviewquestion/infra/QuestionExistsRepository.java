package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.QInterviewQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@RequiredArgsConstructor
public class QuestionExistsRepository {

    private final JPAQueryFactory query;

    public boolean isExist(long questionId, long userId) {
        Integer exist = query.selectOne().
                from(interviewQuestion)
                .where(interviewQuestion.id.eq(questionId), interviewQuestion.owner.id.eq(userId))
                .fetchFirst();

        return exist != null;
    }
}
