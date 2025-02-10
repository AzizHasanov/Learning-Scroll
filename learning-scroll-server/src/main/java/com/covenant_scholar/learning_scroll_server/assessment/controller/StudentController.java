package com.covenant_scholar.learning_scroll_server.assessment.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Course;
import com.covenant_scholar.learning_scroll_server.assessment.repository.CourseRepository;
import com.covenant_scholar.learning_scroll_server.progress.entity.UserSkillProgress;
import com.covenant_scholar.learning_scroll_server.progress.repository.UserSkillProgressRepository;
import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.repository.UserRepository;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSkillProgressRepository userSkillProgressRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@GetMapping("/dashboard")
	public String showDashboard(@RequestParam(required = false) Long courseId, Model model, Principal principal) {
		// Assume you have a method to get the logged-in user by principal name.
		User user = userRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		List<UserSkillProgress> progressList;
		if (courseId != null) {
			// Fetch progress for the specified course by joining through Skill.
			progressList = userSkillProgressRepository.findByUserAndSkillCourseId(user, courseId);
		} else {
			// Fetch all progress for the user.
			progressList = userSkillProgressRepository.findByUser(user);
		}

		// Optionally, add a list of courses for the filter UI.
		List<Course> courseList = courseRepository.findAll();

		model.addAttribute("skillProgressList", progressList);
		model.addAttribute("courseList", courseList);
		return "student/dashboard";
	}
}
