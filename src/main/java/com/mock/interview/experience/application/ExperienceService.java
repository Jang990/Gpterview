package com.mock.interview.experience.application;

import com.mock.interview.experience.domain.Experience;
import com.mock.interview.experience.infra.ExperienceRepository;
import com.mock.interview.experience.presentation.dto.ExperienceForm;
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
    public void create(long userId, ExperienceForm experienceForm) {
        Users users = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Experience experience = Experience.create(users, experienceForm.getExperience());
        repository.save(experience);
    }

}
