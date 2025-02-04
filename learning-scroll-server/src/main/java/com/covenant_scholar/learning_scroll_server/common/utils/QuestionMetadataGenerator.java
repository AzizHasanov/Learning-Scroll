package com.covenant_scholar.learning_scroll_server.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionMetadataGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

	
	public static String generateMCQMetadata(String correctAnswerLabel, String... options) throws Exception {
        List<String> shuffledOptions = new ArrayList<>(Arrays.asList(options));
        Collections.shuffle(shuffledOptions);
        
        Map<String, String> optionsMap = new LinkedHashMap<>();
        String[] labels = {"A", "B", "C", "D"};
        String correctLabel = "";
        
        for (int i = 0; i < shuffledOptions.size(); i++) {
            optionsMap.put(labels[i], shuffledOptions.get(i));
            if (shuffledOptions.get(i).equals(options[Arrays.asList(labels).indexOf(correctAnswerLabel)])) {
                correctLabel = labels[i];
            }
        }
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("options", optionsMap);
        metadata.put("correctAnswer", correctLabel);
        
        return objectMapper.writeValueAsString(metadata);
    }

    public static String generateSequencingMetadata(String correctSequence, String... orderedItems) throws Exception {
        List<String> shuffledList = new ArrayList<>(Arrays.asList(orderedItems));
        Collections.shuffle(shuffledList);
        
        Map<String, String> sequenceMap = new LinkedHashMap<>();
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < shuffledList.size(); i++) {
            sequenceMap.put(labels[i], shuffledList.get(i));
        }
        
        List<String> correctSequenceList = new ArrayList<>();
        for (String label : correctSequence.split(",")) {
            correctSequenceList.add(labels[shuffledList.indexOf(orderedItems[Arrays.asList(labels).indexOf(label.trim())])]);
        }
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sequenceItems", sequenceMap);
        metadata.put("correctSequence", correctSequenceList);
        
        return objectMapper.writeValueAsString(metadata);
    }

    public static void main(String[] args) throws Exception {
        // Example usage:
        String mcqMetadata = generateMCQMetadata("B", "The world was formed by chance", 
            "God created everything with order and purpose", 
            "Humanity is more powerful than nature", 
            "The universe has always existed");
        
        System.out.println("MCQ Metadata: " + mcqMetadata);
        
        String sequencingMetadata = generateSequencingMetadata("A,C,B,D", 
            "God created light", "God created animals", "God separated land and water", "God created humans");
        
        System.out.println("Sequencing Metadata: " + sequencingMetadata);
    }

}
