package com.mock.interview.user.domain;

import com.mock.interview.interview.domain.InterviewStartedEvent;
import com.mock.interview.user.domain.exception.UserNotFoundException;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersEventListener {

    private final UserRepository userRepository;


}
