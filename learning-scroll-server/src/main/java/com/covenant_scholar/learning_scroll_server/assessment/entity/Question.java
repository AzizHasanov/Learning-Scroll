package com.covenant_scholar.learning_scroll_server.assessment.entity;

import com.covenant_scholar.learning_scroll_server.assessment.enums.DifficultyLevel;
import com.covenant_scholar.learning_scroll_server.assessment.enums.QuestionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String text; // The question text

	    @Enumerated(EnumType.STRING)
	    private QuestionType questionType; // Defines the type of question (MCQ, Ordering, etc.)

	    @Enumerated(EnumType.STRING)
	    private DifficultyLevel difficulty; // Easy, Medium, Hard

	    @Column(columnDefinition = "JSON")
	    private String metadata; // Stores additional question-specific data

	    @ManyToOne
	    @JoinColumn(name = "skill_id")
	    private Skill skill; // Links question to a specific skill
}
