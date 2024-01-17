package com.mock.interview.global.security.form;

import com.mock.interview.user.domain.model.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UsersContext extends User {
    private final Users users;
    public UsersContext(Users user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.users = user;
    }
}
