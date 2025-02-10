package com.covenant_scholar.learning_scroll_server.user_management.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.enums.Role;
import com.covenant_scholar.learning_scroll_server.user_management.repository.UserRepository;


@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
	

	public User registerUser(String username, String password, Role role) {
		if (userRepository.existsByUsername(username)) {
			throw new IllegalArgumentException("Username already exists!");
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(role);

		User savedUser = userRepository.save(user);
	    System.out.println("User saved successfully: " + savedUser.getId());
		
		return savedUser;

	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}


	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}