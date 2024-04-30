package com.mock.interview.user.application.session;

import com.mock.interview.global.security.AuthenticationFinder;
import com.mock.interview.global.security.dto.LoginUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final AuthenticationFinder authenticationFinder;
    private final AuthenticationManager authenticationManager;

    // TODO: StackOverflow 예외 수정 필요
    public void updateSession(String changedName) {
        LoginUserDetail loginUserDetail = authenticationFinder.findCurrentAuthenticatedUser();
        if(changedName.equals(loginUserDetail.getName()))
            return;

        loginUserDetail.updateUsername(changedName);
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginUserDetail, null, loginUserDetail.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }
}
