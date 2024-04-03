package com.mock.interview.global.security.form;

import com.mock.interview.user.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderForLoginTest implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // 테스트용.
//        if (username.equals("test") && password.equals("test")) {
//            return new UsernamePasswordAuthenticationToken(Users.createUser(username, password), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        }

        UsersContext usersContext = (UsersContext) userDetailsService.loadUserByUsername(authentication.getName());
        if(!passwordEncoder.matches(password, usersContext.getPassword()))
            throw new BadCredentialsException("잘못된 비밀번호");

        return new UsernamePasswordAuthenticationToken(usersContext.getUsers(), null, usersContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
