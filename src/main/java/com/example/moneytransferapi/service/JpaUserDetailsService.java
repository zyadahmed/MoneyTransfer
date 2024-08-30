package com.example.moneytransferapi.service;

import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.security.UserSecurityAdapter;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user =  userRepository.findUserByEmail(username);
       if (user.isPresent()){
           return new UserSecurityAdapter(user.get());
       }
       throw  new UsernameNotFoundException("User Email Not Found");
    }
}
