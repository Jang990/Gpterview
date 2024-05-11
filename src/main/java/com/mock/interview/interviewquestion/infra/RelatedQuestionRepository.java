package com.mock.interview.interviewquestion.infra;

import com.mock.interview.interviewquestion.domain.model.*;
import com.mock.interview.interviewquestion.infra.recommend.QuestionMetaDataConvertor;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mock.interview.interviewquestion.domain.model.QInterviewQuestion.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelatedQuestionRepository {
    private final JPAQueryFactory query;

    /*
    TODO: Random 질문을 가져오는 쿼리를 제거했다.
        NOT IN 쿼리로 이전에 등장했던 질문이 나오지 않게 됐다.
        ID 순으로 진행되는 슬라이딩 윈도우 방식과 비슷하다.
        지금 방식은 패턴 반복이 있을 수 있기 때문에 -> 특정 주기로 재정렬 시켜주는 작업이 필요하다.
        |
        예를 들어 Question에 randIdx라는 필드를 만들고, 현재 쿼리에서 order by 시킨 후 가져온다.
        이 randIdx는 1시간을 주기로 새로운 랜덤 값으로 초기화해준다.
        이러면 order by를 1시간 전에 보지 못한 새로운 질문들이 등장할 수 있다.
        이런 방식도 생각해봐야 할 것 같다.
     */
    public List<QuestionMetaData> findTechQuestion(long categoryId, long techId, List<Long> appearedQuestionIds, Pageable pageable) {
        QQuestionTechLink link = new QQuestionTechLink("link");
        List<InterviewQuestion> questions = findQueryWithTokens()
                .innerJoin(interviewQuestion.techLink, link).on(link.technicalSubjects.id.eq(techId))
                .where(
                        categoryIdEq(categoryId),
                        typeEq(QuestionType.TECHNICAL),
                        appearedQuestionIdsNotIn(appearedQuestionIds),
                        isOpen()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return QuestionMetaDataConvertor.convert(questions);
    }
    public List<QuestionMetaData> findExperienceQuestion(long experienceId, List<Long> appearedQuestionIds, Pageable pageable) {
        List<InterviewQuestion> questions = findQueryWithTokens()
                .where(
                        experienceIdEq(experienceId),
                        typeEq(QuestionType.EXPERIENCE),
                        appearedQuestionIdsNotIn(appearedQuestionIds),
                        isOpen()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return QuestionMetaDataConvertor.convert(questions);
    }

    public List<QuestionMetaData> findPersonalQuestion(List<Long> appearedQuestionIds, Pageable pageable) {
        List<InterviewQuestion> questions = findQueryWithTokens()
                .where(
                        typeEq(QuestionType.PERSONALITY),
                        appearedQuestionIdsNotIn(appearedQuestionIds),
                        isOpen()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return QuestionMetaDataConvertor.convert(questions);
    }

    private JPAQuery<InterviewQuestion> findQueryWithTokens() {
        return query.selectFrom(interviewQuestion)
                .innerJoin(interviewQuestion.questionToken).fetchJoin();
    }

    private BooleanExpression isOpen() {
        return interviewQuestion.isHidden.isFalse();
    }

    private BooleanExpression categoryIdEq(Long categoryIdCond) {
        return categoryIdCond == null ? null : interviewQuestion.category.id.eq(categoryIdCond);
    }

    private BooleanExpression experienceIdEq(Long experienceIdCond) {
        return experienceIdCond == null ? null : interviewQuestion.experience.id.eq(experienceIdCond);
    }

    private BooleanExpression typeEq(QuestionType typeCond) {
        return typeCond == null ? null : interviewQuestion.questionType.eq(typeCond);
    }

    private BooleanExpression appearedQuestionIdsNotIn(List<Long> appearedQuestionIds) {
        if(appearedQuestionIds == null || appearedQuestionIds.isEmpty())
            return null;
        else
            return interviewQuestion.id.notIn(appearedQuestionIds);
    }
}
