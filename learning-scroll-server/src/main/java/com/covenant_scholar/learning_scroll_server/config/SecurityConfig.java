package com.covenant_scholar.learning_scroll_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/h2-console/**", "/auth/register", "/auth/register/**").permitAll() // Allow
																												// public
																												// routes
				.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/educator/**").hasRole("EDUCATOR")
				.requestMatchers("/student/**").hasRole("STUDENT").anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/auth/register")) // Disable CSRF for H2
				.headers(headers -> headers.frameOptions(frame -> frame.disable())) // Allow frames for H2 Console
				.formLogin(login -> login.loginPage("/auth/login").loginProcessingUrl("/login")
						.successHandler(customLoginSuccessHandler()) // Use the custom handler
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/auth/login").permitAll());

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler customLoginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
}
