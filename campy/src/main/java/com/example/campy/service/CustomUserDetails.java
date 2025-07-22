package com.example.campy.service;

import com.example.campy.entity.Admin;
import com.example.campy.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private User user;
    private Admin admin;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public CustomUserDetails(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user != null) {
            return Collections.singleton(() -> "ROLE_" + user.getRole());
        } else if (admin != null) {
            return Collections.singleton(() -> "ROLE_" + admin.getLevel());
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        if (user != null) {
            return user.getPassword();
        } else if (admin != null) {
            return admin.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (user != null) {
            return user.getEmail();  // email 기반 로그인
        } else if (admin != null) {
            return admin.getUsername(); // admin은 username 기반
        }
        return null;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Integer getUserId() {
        if (user != null) {
            return user.getUserId();
        }
        return null;
    }
}
