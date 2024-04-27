package com.mock.interview.global.security;


import com.mock.interview.global.security.dto.LoginUserDetail;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public final class AuthenticationFinder {
    public LoginUserDetail findCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated())
            throw new AuthenticationCredentialsNotFoundException("인증되지 않은 사용자");
        return (LoginUserDetail) authentication.getPrincipal();
    }
}
