package com.covenant_scholar.learning_scroll_server.assessment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address; // e.g., "John 3:16"
    private String title;   // e.g., "God’s Love"
    
    @Column(columnDefinition = "TEXT")
    private String text;    // The actual passage content
}
