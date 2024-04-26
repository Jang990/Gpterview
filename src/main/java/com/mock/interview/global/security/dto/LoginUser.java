package com.mock.interview.global.security.dto;

import com.mock.interview.user.domain.model.UserRole;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@ToString
public class LoginUser implements UserDetails, OAuth2User {
    @Serial
    private static final long serialVersionUID = 8740389776504078975L;

    @Getter
    private final Long id;
    @Getter
    private final String email;
    private final String username;
    private final String role;

    public LoginUser(Long id, String email, String username, UserRole role) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(username);
        Objects.requireNonNull(role);
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role.toString();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(role));
        return authority;
    }

    // '아니오'는 전부 true
    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 너무 오래 사용한거 아니니?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되어있니? - 휴면계정 관련
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
