package com.covenant_scholar.learning_scroll_server.progress.entity;

import java.util.List;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Skill;
import com.covenant_scholar.learning_scroll_server.user_management.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserSkillProgress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; // Links progress to a specific user

	@ManyToOne
	@JoinColumn(name = "skill_id")
	private Skill skill; // Tracks progress per skill

	private int correctAnswers;
	private int totalAttempts;
	private int totalTimeSpent;
	private boolean completed;

	@OneToMany(mappedBy = "userSkillProgress", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserQuestionProgress> questionProgressList;
}
