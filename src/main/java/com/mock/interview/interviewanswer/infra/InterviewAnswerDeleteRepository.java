package com.mock.interview.interviewanswer.infra;

import com.mock.interview.interviewanswer.domain.model.QInterviewAnswer;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.mock.interview.interviewanswer.domain.model.QInterviewAnswer.*;
import static com.mock.interview.interviewconversationpair.domain.model.QInterviewConversationPair.interviewConversationPair;
import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.interviewQuestion;

@Repository
@Transactional
@RequiredArgsConstructor
public class InterviewAnswerDeleteRepository {
    private final JPAQueryFactory query;

    public long removeExperienceQuestion(long experienceId) {
        return query.update(interviewAnswer)
                .set(interviewAnswer.question, Expressions.nullExpression())
                .where(
                        interviewAnswer.question.id.in(
                                JPAExpressions
                                        .select(interviewQuestion.id)
                                        .from(interviewQuestion)
                                        .where(interviewQuestion.experience.id.eq(experienceId))
                        )
                )
                .execute();
    }
}
