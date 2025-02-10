package com.covenant_scholar.learning_scroll_server.assessment.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Course;
import com.covenant_scholar.learning_scroll_server.assessment.entity.Skill;
import com.covenant_scholar.learning_scroll_server.assessment.repository.CourseRepository;
import com.covenant_scholar.learning_scroll_server.assessment.repository.SkillRepository;
import com.covenant_scholar.learning_scroll_server.progress.entity.UserSkillProgress;
import com.covenant_scholar.learning_scroll_server.progress.repository.UserSkillProgressRepository;
import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.repository.UserRepository;

@Controller
public class CourseController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSkillProgressRepository userSkillProgressRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private SkillRepository skillRepository;
	
	@GetMapping("/student/courses")
	public String showCourses(Model model) {
	    List<Course> courseList = courseRepository.findAll();
	    model.addAttribute("courseList", courseList);
	    return "student/courses";
	}

	@GetMapping("/student/course/{courseId}")
	public String showCourseDetails(@PathVariable Long courseId, Model model, Principal principal) {
	    // Retrieve the course by ID.
	    Course course = courseRepository.findById(courseId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

	    // Retrieve the logged-in user.
	    User user = userRepository.findByUsername(principal.getName())
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    // Retrieve all skills for this course.
	    List<Skill> skillList = skillRepository.findByCourseId(courseId);
	    
	    // Retrieve the student's progress for skills in this course.
	    List<UserSkillProgress> skillProgressList = userSkillProgressRepository.findByUserAndSkillCourseId(user, courseId);

	    // Add attributes to the model.
	    model.addAttribute("course", course);
	    model.addAttribute("skillList", skillList);
	    model.addAttribute("skillProgressList", skillProgressList);
	    
	    return "student/course-details";
	}

}
