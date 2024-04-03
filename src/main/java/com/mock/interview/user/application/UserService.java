package com.mock.interview.user.application;

import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import com.mock.interview.user.presentation.dto.AccountDto;
import com.mock.interview.user.presentation.dto.AccountForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(AccountForm accountForm) {
        Users users = Users.createUser(accountForm.getUsername(), passwordEncoder.encode(accountForm.getPassword()));
        userRepository.save(users);
    }
}
