package com.example.bank.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bank.Entity.User;
import com.example.bank.Repository.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {
    
    @Autowired
    private UserRepo userRepository;
    @Override
    public UserDetails loadUserByUsername(String acc) throws UsernameNotFoundException {
        // 1. Fetch user from DB
        User user = userRepository.findByAccountNumber(acc)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: " + acc));

    
        // 2. Convert your User entity into Spring Security's UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                // .username(user.getUsername())
                .username(acc)  // jwtutil use this on line 20
                // .username(user.getUsername())
                .password(user.getPassword()) // Must be encoded (e.g., BCrypt)
                .roles("USER") // You can set roles/authorities as needed
                .build();
    }
}
