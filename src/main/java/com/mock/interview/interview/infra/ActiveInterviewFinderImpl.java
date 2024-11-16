package com.mock.interview.interview.infra;

import com.mock.interview.interview.domain.ActiveInterviewFinder;
import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActiveInterviewFinderImpl implements ActiveInterviewFinder {

    @Override
    public boolean hasActiveInterview(Users users, LocalDateTime now) {
        return false;
    }
}
