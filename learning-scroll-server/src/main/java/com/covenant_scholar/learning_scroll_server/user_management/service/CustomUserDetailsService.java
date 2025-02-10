package com.covenant_scholar.learning_scroll_server.user_management.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user from the repository
        User user = userRepository.findByUsername(username)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        // Convert the user's role to a GrantedAuthority.
        // Here "ROLE_" is prepend to role enum value.
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
        
        // Return a Spring Security User object.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
}
