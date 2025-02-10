package com.covenant_scholar.learning_scroll_server.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvQuestionsToSqlStatements {

	  public static void main(String[] args) {
	        String csvFilePath = "mapping.csv"; // Path to your CSV file

	        List<String> sqlStatements = processCSV(csvFilePath);

	        // Save to an SQL file (optional)
	        saveSQLToFile(sqlStatements, "insert_questions.sql");
	        sqlStatements.forEach(System.out::println);
	    }

	    private static List<String> processCSV(String filePath) {
	        List<String> sqlStatements = new ArrayList<>();
	        String line;
	        String delimiter = ","; // CSV column separator

	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            // Read the header line (skip it)
	            br.readLine();

	            while ((line = br.readLine()) != null) {
	                String[] row = parseCSVLine(line, delimiter);
	                if (row.length < 15) continue; // Skip invalid rows

	                sqlStatements.addAll(generateSQLStatements(row));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return sqlStatements;
	    }

	    private static String[] parseCSVLine(String line, String delimiter) {
	        // Handles values enclosed in quotes to prevent incorrect splits (e.g., passages with commas)
	        List<String> values = new ArrayList<>();
	        boolean insideQuotes = false;
	        StringBuilder sb = new StringBuilder();

	        for (char ch : line.toCharArray()) {
	            if (ch == '"') {
	                insideQuotes = !insideQuotes;
	            } else if (ch == ',' && !insideQuotes) {
	                values.add(cleanValue(sb.toString().trim()));
	                sb.setLength(0);
	            } else {
	                sb.append(ch);
	            }
	        }
	        values.add(cleanValue(sb.toString().trim())); // Add last value
	        return values.toArray(new String[0]);
	    }

	    private static String cleanValue(String value) {
	        if (value.startsWith("***") && value.endsWith("***")) {
	            value = value.substring(3, value.length() - 3); // Remove triple asterisks
	        }
	        return value.replace("'", "''"); // Escape single quotes for SQL
	    }

	    private static List<String> generateSQLStatements(String[] row) {
	        List<String> sqlStatements = new ArrayList<>();

	        // Insert Passage (if not exists)
	        String passageSql = String.format(
	            "INSERT INTO passage (address, title, text) " +
	            "SELECT '%s', '%s', '%s' " +
	            "WHERE NOT EXISTS (SELECT 1 FROM passage WHERE address='%s' AND title='%s');",
	            row[4], row[5], row[6], row[4], row[5]
	        );
	        sqlStatements.add(passageSql);

	        // Insert Skill (if not exists)
	        String skillSql = String.format(
	            "INSERT INTO skill (name, category, learning_stage, course_id) " +
	            "SELECT '%s', '%s', '%s', (SELECT id FROM course WHERE name='Reading') " +
	            "WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name='%s');",
	            row[1], row[2], row[3], row[1]
	        );
	        sqlStatements.add(skillSql);

	        // Generate Question Metadata
	        String questionMetadata = generateQuestionMetadata(row);

	        // Insert Question
	        String questionSql = String.format(
	            "INSERT INTO question (text, question_type, difficulty, metadata, skill_id, passage_id) " +
	            "VALUES ('%s', '%s', '%s', '%s', " +
	            "(SELECT id FROM skill WHERE name='%s'), " +
	            "(SELECT id FROM passage WHERE address='%s' AND title='%s'));",
	            row[9], row[7], row[8], questionMetadata, row[1], row[4], row[5]
	        );
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

	    private static void saveSQLToFile(List<String> sqlStatements, String fileName) {
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	            for (String sql : sqlStatements) {
	                writer.write(sql);
	                writer.newLine();
	            }
	            System.out.println("SQL statements saved to: " + fileName);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
}
