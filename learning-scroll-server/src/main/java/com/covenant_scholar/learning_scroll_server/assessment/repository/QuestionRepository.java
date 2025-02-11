package com.covenant_scholar.learning_scroll_server.assessment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Question;
import com.covenant_scholar.learning_scroll_server.assessment.entity.Skill;
import com.covenant_scholar.learning_scroll_server.assessment.enums.QuestionType;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
    List<Question> findBySkillAndQuestionType(Skill skill, QuestionType questionType);

}
