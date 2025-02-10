package com.covenant_scholar.learning_scroll_server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		String redirectURL = "/";

		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();

			if (role.equals("ROLE_ADMIN")) {
				response.sendRedirect("/admin/dashboard");
				return;
			} else if (role.equals("ROLE_EDUCATOR")) {
				response.sendRedirect("/educator/dashboard");
				return;
			} else if (role.equals("ROLE_STUDENT")) {
				response.sendRedirect("/student/dashboard");
				return;
			}
		}

		response.sendRedirect(redirectURL);
	}
}
