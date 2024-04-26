package com.mock.interview.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (authz) ->
                                authz
                                        .requestMatchers("/interview/**", "/profile/**").authenticated()
                                        .anyRequest().permitAll()
                )
                .logout(
                        (logout) ->
                                logout.logoutSuccessUrl("/")
                )
                .oauth2Login(
                        (login) ->
                                login.userInfoEndpoint( // 로그인 이후 사용자 정보를 가져와서 다루기 위한 설정
                                                config -> config.userService(customOAuth2UserService) // 소셜 로그인 성공 이후 후처리 (계정 등록 등등)
                                        )
                                        .loginPage("/login")
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
