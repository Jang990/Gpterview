package com.mock.interview.user.infrastructure;

import com.mock.interview.category.application.helper.CategoryConvertor;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.interview.presentation.dto.InterviewAccountForm;
import com.mock.interview.tech.application.helper.TechConvertHelper;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import com.mock.interview.user.application.helper.UserConvertor;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import com.mock.interview.user.presentation.dto.AccountDetailDto;
import com.mock.interview.user.presentation.dto.AccountUpdateForm;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mock.interview.category.domain.model.QJobCategory.*;
import static com.mock.interview.category.domain.model.QJobPosition.*;
import static com.mock.interview.user.domain.model.QUsers.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRepositoryForView {
    private final JPAQueryFactory query;

    public AccountDetailDto findUserDetail(long userId) {
        Users result = findUserView(userId);
        return new AccountDetailDto(
                result.getId(),
                result.getUsername(),
                result.getEmail(),
                result.getPicture(),
                CategoryConvertor.convert(result.getCategory()),
                CategoryConvertor.convert(result.getPosition()),
                result.getCreatedAt(),
                TechConvertHelper.convertView(result.getTechLink().stream().map(UsersTechLink::getTechnicalSubjects).toList())
//                convertTech(result),
//                convertExperiences(result)
        );
    }

    public AccountUpdateForm findUserUpdateForm(long userId) {
        Users result = query.selectFrom(users)
                .leftJoin(users.category, jobCategory).fetchJoin()
                .leftJoin(users.position, jobPosition).fetchJoin()
                .where(userIdEq(userId))
                .fetchOne();
        return UserConvertor.convertInfo(result);
    }

    public InterviewAccountForm findUserInterviewForm(long userId) {
        Users result = findUserView(userId);
        return new InterviewAccountForm(
                CategoryConvertor.convertSelectedJobCategoryView(result),
                convertTech(result),
                convertExperiences(result)
        );
    }

    public List<TechViewDto> findUserTech(long userId) {
        Users result = query.selectFrom(users)
                .where(userIdEq(userId))
                .fetchOne();
        return TechConvertHelper.convertView(
                result.getTechLink().stream()
                        .map(UsersTechLink::getTechnicalSubjects).toList());
    }

    private Users findUserView(Long userIdCond) {
        return query.selectFrom(users)
                .leftJoin(users.category, jobCategory).fetchJoin()
                .leftJoin(users.position, jobPosition).fetchJoin()
                .where(userIdEq(userIdCond))
                .fetchOne();
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : users.username.eq(username);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : users.id.eq(userId);
    }

    private List<TechViewDto> convertTech(Users users) {
        return users.getTechLink().stream().map(UsersTechLink::getTechnicalSubjects)
                .map((tech) -> new TechViewDto(tech.getId(), tech.getName())).toList();
    }

    private List<ExperienceDto> convertExperiences(Users users) {
        return users.getExperiences().stream().map(exp -> new ExperienceDto(exp.getId(), exp.getContent())).toList();
    }
}
