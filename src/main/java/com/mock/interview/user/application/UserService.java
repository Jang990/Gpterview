package com.mock.interview.user.application;

import com.mock.interview.category.infra.JobCategoryRepository;
import com.mock.interview.category.infra.JobPositionRepository;
import com.mock.interview.tech.domain.model.TechnicalSubjects;
import com.mock.interview.tech.infra.TechnicalSubjectsRepository;
import com.mock.interview.user.application.helper.UserUpdateHelper;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.domain.model.UsersTechLink;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.user.infrastructure.UsersTechLinkRepository;
import com.mock.interview.user.presentation.dto.AccountUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JobCategoryRepository categoryRepository;
    private final JobPositionRepository positionRepository;
    private final TechnicalSubjectsRepository technicalSubjectsRepository;
    private final UsersTechLinkRepository usersTechLinkRepository;

    public void update(AccountUpdateForm form) {
        Users users = repository.findById(form.getUserId())
                .orElseThrow(UserNotFoundException::new);

        UserUpdateHelper.updateCategory(users, categoryRepository, form.getCategoryId());
        UserUpdateHelper.updatePosition(users, positionRepository, form.getPositionId());

        String changedUsername = form.getUsername();
        if(users.getUsername().equals(changedUsername))
            return;
        users.changeUsername(changedUsername);
    }

    public void replaceTech(long userId, List<Long> techRequest) {
        Users users = repository.findWithTech(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Long> existsTechIds = users.getTechLink().stream()
                .map(UsersTechLink::getTechnicalSubjects)
                .map(TechnicalSubjects::getId).toList();

        List<Long> newTechIds = techRequest.stream()
                .filter(newId -> !existsTechIds.contains(newId)).toList();
        if (!newTechIds.isEmpty()) {
            List<TechnicalSubjects> newTechList = technicalSubjectsRepository
                    .findAllById(newTechIds);
            users.linkTech(newTechList);
        }

        List<Long> techIdsToDelete = existsTechIds.stream()
                .filter((existsId) -> !techRequest.contains(existsId)).toList();
        if(!techIdsToDelete.isEmpty())
            usersTechLinkRepository.deleteUserTech(userId, techIdsToDelete);
    }
}
