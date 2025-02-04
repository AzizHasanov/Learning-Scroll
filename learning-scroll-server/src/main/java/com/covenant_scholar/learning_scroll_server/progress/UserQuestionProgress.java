package com.covenant_scholar.learning_scroll_server.progress;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Question;
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
public class UserQuestionProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Links progress to a specific user

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // Tracks progress per question

    @ManyToOne
    @JoinColumn(name = "user_skill_progress_id")
    private UserSkillProgress userSkillProgress; // Links question progress to overall skill progress

    private boolean answeredCorrectly;
    private int attemptCount;
    private int timeSpent;
}
