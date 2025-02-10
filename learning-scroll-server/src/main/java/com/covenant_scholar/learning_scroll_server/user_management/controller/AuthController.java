package com.covenant_scholar.learning_scroll_server.user_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.enums.Role;
import com.covenant_scholar.learning_scroll_server.user_management.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "auth/register";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam Role role,
			RedirectAttributes redirectAttributes) {
		// Check if username already exists
		if (userService.existsByUsername(username)) {
			redirectAttributes.addFlashAttribute("usernameTaken", true);
			redirectAttributes.addFlashAttribute("username", username); // Keep entered username
			return "redirect:/auth/register";
		}

		// Check if password is strong enough
		if (!isStrongPassword(password)) {
			redirectAttributes.addFlashAttribute("weakPassword", true);
			redirectAttributes.addFlashAttribute("username", username); // Keep entered username
			return "redirect:/auth/register";
		}

		// Register the user
		userService.registerUser(username, password, role);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/auth/register";
	}

	private boolean isStrongPassword(String password) {
		return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[A-Z].*");
	}

	@GetMapping("/login")
	public String showLoginForm(@RequestParam(value = "success", required = false) String success, Model model) {
		if (success != null) {
			model.addAttribute("successMessage", "Registration successful! You can now log in.");
		}
		return "auth/login"; // Make sure login.html exists in templates/auth/
	}
}
