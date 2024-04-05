package com.mock.interview.user.application;

import com.mock.interview.category.domain.exception.JobCategoryNotFoundException;
import com.mock.interview.category.domain.model.JobCategory;
import com.mock.interview.category.domain.model.JobPosition;
import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import com.mock.interview.user.domain.UsersTechLinker;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.user.presentation.dto.AccountForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPositionRepository jobPositionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersTechLinker usersTechLinker;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;

    public void create(AccountForm form, List<Long> relatedTechIds) {
        List<TechnicalSubjects> techs = technicalSubjectsRepository.findAllById(relatedTechIds);
        Users users = Users.createUser(form.getUsername(), passwordEncoder.encode(form.getPassword()));
        usersTechLinker.lineUniqueTech(users, techs);
        userRepository.save(users);

        if (hasPosition(form)) {
            JobPosition position = jobPositionRepository.findWithCategory(form.getCategoryId(), form.getPositionId())
                    .orElseThrow(JobCategoryNotFoundException::new);
            users.linkJob(position.getCategory(), position);
            return;
        }

        if (hasCategory(form)) {
            JobCategory category = jobCategoryRepository.findById(form.getCategoryId())
                    .orElseThrow(JobCategoryNotFoundException::new);
            users.linkCategory(category);
        }
    }

    private boolean hasCategory(AccountForm form) {
        return form.getCategoryId() != null;
    }

    private boolean hasPosition(AccountForm form) {
        return form.getPositionId() != null;
    }
}
