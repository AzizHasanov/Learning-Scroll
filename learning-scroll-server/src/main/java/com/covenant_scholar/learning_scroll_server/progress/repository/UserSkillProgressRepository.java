package com.covenant_scholar.learning_scroll_server.progress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.covenant_scholar.learning_scroll_server.progress.entity.UserSkillProgress;
import com.covenant_scholar.learning_scroll_server.user_management.entity.User;

@Repository
public interface UserSkillProgressRepository extends JpaRepository<UserSkillProgress, Long> {

    List<UserSkillProgress> findByUser(User user);
    List<UserSkillProgress> findByUserAndSkillCourseId(User user, Long courseId);    
}
