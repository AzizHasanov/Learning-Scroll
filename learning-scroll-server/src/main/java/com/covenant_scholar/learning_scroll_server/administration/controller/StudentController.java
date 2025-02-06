package com.covenant_scholar.learning_scroll_server.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    @GetMapping("/dashboard")
    public String studentDashboard() {
        return "student/dashboard"; // Thymeleaf template for student
    }
}
