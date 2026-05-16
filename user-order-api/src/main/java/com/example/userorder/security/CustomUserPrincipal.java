package com.example.userorder.security;

import com.example.userorder.domain.user.Role;
import com.example.userorder.domain.user.vo.LoginId;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserPrincipal implements UserDetails {
    private final Long id;
    private final LoginId loginId;
    private final Role role;

    public CustomUserPrincipal(JwtUserInfo jwtUserInfo) {
        this.id = jwtUserInfo.id();
        this.loginId = jwtUserInfo.loginId();
        this.role = jwtUserInfo.role();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return loginId.value();
    }
}
