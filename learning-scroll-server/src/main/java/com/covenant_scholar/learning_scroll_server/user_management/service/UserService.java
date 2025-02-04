package com.covenant_scholar.learning_scroll_server.user_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.enums.Role;
import com.covenant_scholar.learning_scroll_server.user_management.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public User registerUser(String username, String password, Role role) {
		if (userRepository.existsByUsername(username)) {
			throw new IllegalArgumentException("Username already exists!");
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(role);

		return userRepository.save(user);

	}

	@Autowired
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}
