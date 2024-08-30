package com.example.moneytransferapi.security;

import com.example.moneytransferapi.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserSecurityAdapter implements UserDetails
{
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(()->user.getRole().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
