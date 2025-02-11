package com.covenant_scholar.learning_scroll_server.assessment.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.covenant_scholar.learning_scroll_server.assessment.entity.Question;
import com.covenant_scholar.learning_scroll_server.assessment.entity.Skill;
import com.covenant_scholar.learning_scroll_server.assessment.enums.QuestionType;
import com.covenant_scholar.learning_scroll_server.assessment.repository.QuestionRepository;
import com.covenant_scholar.learning_scroll_server.assessment.repository.SkillRepository;
import com.covenant_scholar.learning_scroll_server.progress.entity.UserQuestionProgress;
import com.covenant_scholar.learning_scroll_server.progress.entity.UserSkillProgress;
import com.covenant_scholar.learning_scroll_server.progress.repository.UserQuestionProgressRepository;
import com.covenant_scholar.learning_scroll_server.progress.repository.UserSkillProgressRepository;
import com.covenant_scholar.learning_scroll_server.user_management.entity.User;
import com.covenant_scholar.learning_scroll_server.user_management.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/student/assessment")
public class AssessmentController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillProgressRepository userSkillProgressRepository;
    
    @Autowired
    private UserQuestionProgressRepository userQuestionProgressRepository;
    
    @Autowired
    private UserRepository userRepository;

    // GET method to load the assessment page for a given skill and question index.
    @GetMapping("/{skillId}")
    public String showAssessment(@PathVariable Long skillId,
                                 @RequestParam(required = false, defaultValue = "0") int questionIndex,
                                 Model model,
                                 Principal principal) {
        // Retrieve the logged-in user.
        User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch the skill.
        Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));

        // Fetch MCQ questions for that skill.
        List<Question> questions = questionRepository.findBySkillAndQuestionType(skill, QuestionType.MCQ);
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No questions available for this skill");
        }

        // Ensure questionIndex is within bounds.
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            // Redirect to a summary page if finished (implementation later).
            return "redirect:/student/assessment/" + skillId + "/summary";
        }

        Question question = questions.get(questionIndex);

        // Parse options from question metadata (JSON format).
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Expected metadata format:
            // {"options":{"A":"Humanity is more powerful than nature",
            //             "B":"The world was formed by chance",
            //             "C":"God created everything with order and purpose",
            //             "D":"The universe has always existed"},
            //  "correctAnswer":"C"}
            String metadata = question.getMetadata();
            // If metadata is double-encoded (starts and ends with quotes), strip them and unescape inner quotes.
            if (metadata.startsWith("\"") && metadata.endsWith("\"")) {
                metadata = metadata.substring(1, metadata.length() - 1).replace("\\\"", "\"");
            }
            Map<String, Object> metaMap = mapper.readValue(metadata, new TypeReference<Map<String, Object>>() {});
            
            @SuppressWarnings("unchecked")
            Map<String, String> optionsMap = (Map<String, String>) metaMap.get("options");
            List<Map.Entry<String, String>> optionEntries = new ArrayList<>(optionsMap.entrySet());
            optionEntries.sort(Map.Entry.comparingByKey());
            model.addAttribute("optionEntries", optionEntries);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing question metadata", e);
        }

        // Retrieve or create UserSkillProgress for the user and skill.
        Optional<UserSkillProgress> optionalUSP = userSkillProgressRepository.findByUserAndSkillId(user, skillId);
        UserSkillProgress usp = optionalUSP.orElseGet(() -> {
            UserSkillProgress newUsp = new UserSkillProgress();
            newUsp.setUser(user);
            newUsp.setSkill(skill);
            newUsp.setCorrectAnswers(0);
            newUsp.setTotalAttempts(0);
            newUsp.setTotalTimeSpent(0);
            newUsp.setCompleted(false);
            return userSkillProgressRepository.save(newUsp);
        });

        // Calculate progress stats.
        int questionsAnswered = usp.getCorrectAnswers();
        int timeElapsed = usp.getTotalTimeSpent();
        int smartScore = 0; // Placeholder for smartScore calculation.

        // Add attributes to the model.
        model.addAttribute("skill", skill);
        model.addAttribute("question", question);
        model.addAttribute("questionIndex", questionIndex);
        model.addAttribute("totalQuestions", questions.size());
        model.addAttribute("questionsAnswered", questionsAnswered);
        model.addAttribute("timeElapsed", timeElapsed);
        model.addAttribute("smartScore", smartScore);

        return "student/assessment";
    }

    // POST method to handle answer submission.
    @PostMapping("/submit")
    public String submitAnswer(@RequestParam Long questionId,
                               @RequestParam Long skillId,
                               @RequestParam int questionIndex,
                               @RequestParam String selectedOption,
                               Principal principal) {
        // Retrieve the logged-in user.
        User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch the question and skill.
        Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));

        // Parse the correct answer from question metadata (JSON format).
        boolean isCorrect = false;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String metadata = question.getMetadata();
            if (metadata.startsWith("\"") && metadata.endsWith("\"")) {
                metadata = metadata.substring(1, metadata.length() - 1).replace("\\\"", "\"");
            }
            Map<String, Object> metaMap = mapper.readValue(metadata, new TypeReference<Map<String, Object>>() {});
           
            String correctAnswer = (String) metaMap.get("correctAnswer");
            isCorrect = selectedOption.equals(correctAnswer);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing question metadata", e);
        }

        // Update UserSkillProgress.
        UserSkillProgress usp = userSkillProgressRepository.findByUserAndSkillId(user, skillId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Progress not found"));
        usp.setTotalAttempts(usp.getTotalAttempts() + 1);
        if (isCorrect) {
            usp.setCorrectAnswers(usp.getCorrectAnswers() + 1);
        }
        // (Time elapsed update would occur here.)
        userSkillProgressRepository.save(usp);

        // Save a new UserQuestionProgress record.
        UserQuestionProgress uqp = new UserQuestionProgress();
        uqp.setUser(user);
        uqp.setQuestion(question);
        uqp.setUserSkillProgress(usp);
        uqp.setAnsweredCorrectly(isCorrect);
        uqp.setAttemptCount(1); // Simplified: one attempt per question.
        uqp.setTimeSpent(0);    // Update with actual timing if needed.
        userQuestionProgressRepository.save(uqp);

        // Redirect to a feedback page (to be implemented) with parameters.
        return "redirect:/student/feedback?skillId=" + skillId + "&questionIndex=" + questionIndex + "&correct=" + isCorrect;
    }
    
    @GetMapping("/{skillId}/summary")
    public String showSkillSummary(@PathVariable Long skillId, Model model, Principal principal) {
        // Retrieve the logged-in user.
        User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch the skill.
        Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));

        // Retrieve the user's progress for the skill.
        UserSkillProgress usp = userSkillProgressRepository.findByUserAndSkillId(user, skillId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Progress not found"));

        int totalAttempts = usp.getTotalAttempts();
        int correctAnswers = usp.getCorrectAnswers();
        int totalTimeSpent = usp.getTotalTimeSpent();
        int smartScore = 0; // Placeholder for the smartScore calculation.

        // Add attributes to the model.
        model.addAttribute("skill", skill);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        model.addAttribute("smartScore", smartScore);

        return "student/skill-summary";
    }
}
