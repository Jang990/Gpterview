package com.mock.interview.global.security;


import com.mock.interview.user.domain.model.Users;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public final class AuthenticationFinder {
    private AuthenticationFinder() {}

    public Users findAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationCredentialsNotFoundException("인증되지 않은 사용자");
        return (Users) authentication.getPrincipal();
    }
}
