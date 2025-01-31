package com.covenant_scholar.learning_scroll_server.assessment.entity;

import java.util.List;

import com.covenant_scholar.learning_scroll_server.common.enums.DifficultyLevel;

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
public class SkillInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
    
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;
    
    @OneToMany(mappedBy = "skillInstance")
    private List<Question> questions;
}
