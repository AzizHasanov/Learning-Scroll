package com.covenant_scholar.learning_scroll_server.assessment.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/student/feedback")
public class FeedbackController {

	 // A list of positive messages for correct answers (biblically inspired).
    private final List<String> positiveMessages = Arrays.asList(
            "Well done, child of God!",
            "Your wisdom shines bright – keep it up!",
            "God is with you every step – excellent work!",
            "Your answer reflects His truth. Amazing!",
            "Blessings upon your insight – great job!",
            "You are on the path of righteousness. Well done!",
            "Correct! Your faith and knowledge inspire us.",
            "Your light shines through – keep seeking His wisdom!",
            "Marvelous! May God's grace guide your steps.",
            "Excellent work – God has great plans for you!"
    );

    // A list of encouraging messages for incorrect answers.
    private final List<String> negativeMessages = Arrays.asList(
            "Even the faithful stumble – learn and grow.",
            "A misstep today is a lesson for tomorrow.",
            "Don't be discouraged; God's mercy is abundant.",
            "Every error teaches wisdom – keep moving forward.",
            "Though this one was missed, His love remains with you.",
            "Take heart – every lesson brings you closer to truth.",
            "A stumble is not the end; it's a stepping stone to greater things.",
            "Reflect, learn, and rise – His grace will guide you.",
            "Mistakes pave the way for understanding – keep the faith.",
            "Your journey is filled with lessons; trust in His plan."
    );

    @GetMapping
    public String showFeedback(@RequestParam Long skillId,
                               @RequestParam int questionIndex,
                               @RequestParam boolean correct,
                               Model model) {
        Random random = new Random();
        String feedbackMessage;
        if (correct) {
            feedbackMessage = positiveMessages.get(random.nextInt(positiveMessages.size()));
        } else {
            feedbackMessage = negativeMessages.get(random.nextInt(negativeMessages.size()));
        }
        model.addAttribute("feedbackMessage", feedbackMessage);
        model.addAttribute("skillId", skillId);
        model.addAttribute("questionIndex", questionIndex);
        return "student/feedback";
    }
}