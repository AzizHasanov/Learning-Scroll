package com.covenant_scholar.learning_scroll_server.assessment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

	List<Skill> findByCourseId(Long courseId);

}
