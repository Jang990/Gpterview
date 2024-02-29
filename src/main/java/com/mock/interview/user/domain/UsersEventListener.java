package com.mock.interview.user.domain;

import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersEventListener {

    private final UserRepository userRepository;


}
