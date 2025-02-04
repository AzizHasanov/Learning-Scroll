package com.covenant_scholar.learning_scroll_server.assessment.entity;

import java.util.List;

import com.covenant_scholar.learning_scroll_server.assessment.enums.LearningStage;
import com.covenant_scholar.learning_scroll_server.assessment.enums.SkillCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Skill {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Skill name (e.g., "Inference", "Context Clues")

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // Links skill to a subject

    @Enumerated(EnumType.STRING)
    private SkillCategory category; // Category classification

    @Enumerated(EnumType.STRING)
    private LearningStage learningStage; // Foundational, Intermediate, Advanced, Master
    
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions; // List of questions under this skill
}
