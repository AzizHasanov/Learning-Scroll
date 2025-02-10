package com.covenant_scholar.learning_scroll_server.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Course;


public interface CourseRepository extends JpaRepository<Course, Long> {

}
