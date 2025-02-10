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
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/auth/register", "/auth/register/**", "/auth/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/educator/**").hasRole("EDUCATOR")
                .requestMatchers("/student/**").hasRole("STUDENT")
                .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/auth/register"))
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
        .formLogin(login -> login
                // Set the login page to /auth/login so that GET requests return your login view.
                .loginPage("/auth/login")
                // Set the processing URL to /auth/login so that POST requests are intercepted by Spring Security.
                .loginProcessingUrl("/auth/login")
                .failureUrl("/auth/login?error=true")  // explicitly use /auth/login on failure
                .successHandler(customLoginSuccessHandler())
                .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                .permitAll()
        );

        return http.build();
    }

	@Bean
	public AuthenticationSuccessHandler customLoginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
}
