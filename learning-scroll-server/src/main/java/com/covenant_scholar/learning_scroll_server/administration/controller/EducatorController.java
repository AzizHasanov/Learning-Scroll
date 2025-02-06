package com.covenant_scholar.learning_scroll_server.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/educator")
public class EducatorController {
    @GetMapping("/dashboard")
    public String educatorDashboard() {
        return "educator/dashboard"; // Thymeleaf template for educator
    }
}
