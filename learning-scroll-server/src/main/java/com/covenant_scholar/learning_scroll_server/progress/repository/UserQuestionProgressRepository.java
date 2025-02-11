package com.covenant_scholar.learning_scroll_server.progress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.covenant_scholar.learning_scroll_server.progress.entity.UserQuestionProgress;

public interface UserQuestionProgressRepository extends JpaRepository<UserQuestionProgress, Long> {

}
