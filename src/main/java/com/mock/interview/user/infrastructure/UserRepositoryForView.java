package com.mock.interview.user.infrastructure;

import com.mock.interview.category.presentation.dto.JobCategoryView;
import com.mock.interview.experience.presentation.dto.ExperienceDto;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import com.mock.interview.user.domain.model.QUsers;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import com.mock.interview.user.presentation.dto.AccountDetailDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.mock.interview.category.domain.model.QJobCategory.*;
import static com.mock.interview.category.domain.model.QJobPosition.*;
import static com.mock.interview.tech.domain.model.QTechnicalSubjects.*;
import static com.mock.interview.user.domain.model.QExperience.*;
import static com.mock.interview.user.domain.model.QUsersTechLink.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRepositoryForView {
    private final JPAQueryFactory query;

    public AccountDetailDto findUserDetail(String username) {
        Users users = query.selectFrom(QUsers.users)
                .leftJoin(QUsers.users.experiences, experience)
                .leftJoin(QUsers.users.category, jobCategory)
                .leftJoin(QUsers.users.position, jobPosition)
                .leftJoin(QUsers.users.techLink, usersTechLink)
                .leftJoin(usersTechLink.technicalSubjects, technicalSubjects)
                .where(QUsers.users.username.eq(username))
                .fetchOne();

        return new AccountDetailDto(
                users.getUsername(),
                users.getCreatedAt(),
                new JobCategoryView(
                        users.getCategory() == null ? null : users.getCategory().getName(),
                        users.getPosition() == null ? null : users.getPosition().getName()
                ),
                users.getTechLink().stream().map(UsersTechLink::getTechnicalSubjects).map((tech) -> new TechViewDto(tech.getId(), tech.getName())).toList(),
                users.getExperiences().stream().map(exp -> new ExperienceDto(exp.getId(), exp.getContent())).toList()
        );
    }
}
