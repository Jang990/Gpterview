package com.mock.interview.experience.application;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.domain.exception.ExperienceNotFoundException;
import com.mock.interview.experience.infra.ExperienceExistsRepository;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.experience.infra.InterviewExperienceLinkRepository;
import com.mock.interview.experience.presentation.dto.ExperienceBulkForm;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExperienceService {
    private final UserRepository userRepository;
    private final ExperienceRepository repository;
    public void create(String username, ExperienceBulkForm experienceBulkForm) {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        List<Experience> list = experienceBulkForm.getExperience().stream()
                .map(experienceContent -> Experience.create(users, experienceContent))
                .collect(Collectors.toList());
        repository.saveAll(list);
    }

}
