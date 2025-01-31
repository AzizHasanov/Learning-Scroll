package com.covenant_scholar.learning_scroll_server;

import com.covenant_scholar.learning_scroll_server.assessment.entity.SkillInstance;
import com.covenant_scholar.learning_scroll_server.user_management.entity.User;

import jakarta.persistence.Entity;
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
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "skill_instance_id")
    private SkillInstance skillInstance;
    
    private int correctAnswers;
    private int totalAttempts;
    private int totalTimeSpent;
    private boolean completed;
}