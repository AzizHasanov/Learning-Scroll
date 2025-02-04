package com.covenant_scholar.learning_scroll_server.common.utils;

import java.util.ArrayList;
import java.util.List;

public class GoogleSheetsToSQL {

	// private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) {
		// Example row data from Google Sheets
		String[] row = { "S1", "Identifying Main Ideas", "Basic Comprehension", "Foundational", "Genesis 1:1–31",
				"The Creation of the World", "In the beginning, God created the heavens and the earth...", "MCQ",
				"Basic", "Which best summarizes the creation account?", "The world was formed by chance",
				"God created everything with order and purpose", "Humanity is more powerful than nature",
				"The universe has always existed", "B" };

		// Generate SQL insert statements
		List<String> sqlStatements = generateSQLStatements(row);

		// Print the SQL statements
		sqlStatements.forEach(System.out::println);
	}

	private static List<String> generateSQLStatements(String[] row) {
		/*
		row[0] Skill Code
		row[1] Skill Name					Skill        
		row[2] Skill Category				Skill
		row[3] Learning Stage				Skill
		row[4] Passage Address				Passage
		row[5] Passage Title				Passage	
		row[6] Passage Text					Passage
		row[7] Question Type				Question
		row[8] Question Difficulty Level	Question
		row[9] Question Text				Question
		row[10] Option A					Metadata
		row[11] Option B					Metadata
		row[12] Option C					Metadata
		row[13] Option D					Metadata
		row[14] Correct Answer				Metadata
		*/
		List<String> sqlStatements = new ArrayList<>();

		// Insert Passage
		String passageSql = String.format("INSERT INTO passage (address, title, text) VALUES ('%s', '%s', '%s');",
				row[4], row[5], row[6]);
		sqlStatements.add(passageSql);

		// Insert Skill
		String skillSql = String.format(
				"INSERT INTO skill (name, category, learning_stage, subject_id) VALUES ('%s', '%s', '%s', (SELECT id FROM subject WHERE name='Reading'));",
				row[1], row[2], row[3]);
		sqlStatements.add(skillSql);

		// Insert Question
		String questionMetadata = generateQuestionMetadata(row);
		String questionSql = String.format(
				"INSERT INTO question (text, question_type, difficulty, metadata, skill_id, passage_id) "
						+ "VALUES ('%s', '%s', '%s', '%s', (SELECT id FROM skill WHERE name='%s'), (SELECT id FROM passage WHERE address='%s'));",
				row[9], row[7], row[8], questionMetadata, row[1], row[4]);
		sqlStatements.add(questionSql);

		return sqlStatements;
	}

	 private static String generateQuestionMetadata(String[] row) {
	        try {
	            if (row[7].equals("MCQ")) {
	                return QuestionMetadataGenerator.generateMCQMetadata(row[14], row[10], row[11], row[12], row[13]);
	            } else if (row[7].equals("SEQUENCING")) {
	                return QuestionMetadataGenerator.generateSequencingMetadata(row[14], row[10], row[11], row[12], row[13]);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "{}";
	    }
	
	

}
