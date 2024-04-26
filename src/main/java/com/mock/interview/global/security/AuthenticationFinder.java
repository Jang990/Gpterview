package com.mock.interview.global.security;


import com.mock.interview.global.security.dto.LoginUser;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public final class AuthenticationFinder {
    public LoginUser findAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationCredentialsNotFoundException("인증되지 않은 사용자");
        return (LoginUser) authentication.getPrincipal();
    }

}
