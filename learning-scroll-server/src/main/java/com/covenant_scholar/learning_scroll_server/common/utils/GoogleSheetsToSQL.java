package com.covenant_scholar.learning_scroll_server.common.utils;

import java.util.ArrayList;
import java.util.List;

public class GoogleSheetsToSQL {

	public static void main(String[] args) {
        // Example row data from Google Sheets
        String[] row = {
            "S1", "Identifying Main Ideas", "Basic Comprehension", "Foundational",
            "Genesis 1:1–31", "The Creation of the World", "In the beginning, God created the heavens and the earth...",
            "MCQ", "Basic", "Which best summarizes the creation account?",
            "The world was formed by chance", "God created everything with order and purpose",
            "Humanity is more powerful than nature", "The universe has always existed", "B"
        };
        String[] row2 = {
                "S4", "Sequencing Events",	"Text Structure and Literary Analysis",	"Foundational",	"Genesis 1:1–31",	"The Creation Account",	
                "***In the beginning, God created the heavens and the earth. The earth was without form and void, and darkness was over the face of the deep. And the Spirit of God was hovering over the face of the waters. And God said, “Let there be light,” and there was light. And God saw that the light was good. And God separated the light from the darkness. God called the light Day, and the darkness he called Night. And there was evening and there was morning, the first day. And God said, “Let there be an expanse in the midst of the waters, and let it separate the waters from the waters.” And God made the expanse and separated the waters that were under the expanse from the waters that were above the expanse. And it was so. And God called the expanse Heaven. And there was evening and there was morning, the second day. And God said, “Let the waters under the heavens be gathered together into one place, and let the dry land appear.” And it was so. God called the dry land Earth, and the waters that were gathered together he called Seas. And God saw that it was good. And God said, “Let the earth sprout vegetation, plants yielding seed, and fruit trees bearing fruit in which is their seed, each according to its kind, on the earth.” And it was so. The earth brought forth vegetation, plants yielding seed according to their own kinds, and trees bearing fruit in which is their seed, each according to its kind. And God saw that it was good. And there was evening and there was morning, the third day. And God said, “Let there be lights in the expanse of the heavens to separate the day from the night. And let them be for signs and for seasons, and for days and years, and let them be lights in the expanse of the heavens to give light upon the earth.” And it was so. And God made the two great lights—the greater light to rule the day and the lesser light to rule the night—and the stars. And God set them in the expanse of the heavens to give light on the earth, to rule over the day and over the night, and to separate the light from the darkness. And God saw that it was good. And there was evening and there was morning, the fourth day. And God said, “Let the waters swarm with swarms of living creatures, and let birds fly above the earth across the expanse of the heavens.” So God created the great sea creatures and every living creature that moves, with which the waters swarm, according to their kinds, and every winged bird according to its kind. And God saw that it was good. And God blessed them, saying, “Be fruitful and multiply and fill the waters in the seas, and let birds multiply on the earth.” And there was evening and there was morning, the fifth day. And God said, “Let the earth bring forth living creatures according to their kinds—livestock and creeping things and beasts of the earth according to their kinds.” And it was so. And God made the beasts of the earth according to their kinds and the livestock according to their kinds, and everything that creeps on the ground according to its kind. And God saw that it was good. Then God said, “Let us make man in our image, after our likeness. And let them have dominion over the fish of the sea and over the birds of the heavens and over the livestock and over all the earth and over every creeping thing that creeps on the earth.” So God created man in his own image, in the image of God he created him; male and female he created them. And God blessed them. And God said to them, “Be fruitful and multiply and fill the earth and subdue it, and have dominion over the fish of the sea and over the birds of the heavens and over every living thing that moves on the earth.” And God said, “Behold, I have given you every plant yielding seed that is on the face of all the earth, and every tree with seed in its fruit. You shall have them for food. And to every beast of the earth and to every bird of the heavens and to everything that creeps on the earth, everything that has the breath of life, I have given every green plant for food.” And it was so. And God saw everything that he had made, and behold, it was very good. And there was evening and there was morning, the sixth day.***",	
                "SEQUENCING",	"Basic",	"Arrange these creation events in the correct order:",	"God created light",	"God created animals",	"God separated land and water",	
                "God created humans",	"A, C, B, D"
        };
        // Generate SQL insert statements
        List<String> sqlStatements = generateSQLStatements(row);

        // Print the SQL statements
        sqlStatements.forEach(System.out::println);
    }

    private static List<String> generateSQLStatements(String[] row) {
        /*
        row[0] Skill Code
        row[1] Skill Name                 Skill        
        row[2] Skill Category              Skill
        row[3] Learning Stage              Skill
        row[4] Passage Address             Passage
        row[5] Passage Title               Passage    
        row[6] Passage Text                Passage
        row[7] Question Type               Question
        row[8] Question Difficulty Level   Question
        row[9] Question Text               Question
        row[10] Option A                   Metadata
        row[11] Option B                   Metadata
        row[12] Option C                   Metadata
        row[13] Option D                   Metadata
        row[14] Correct Answer             Metadata
        */
        List<String> sqlStatements = new ArrayList<>();

        // Insert Passage (if not exists)
        String passageSql = String.format(
            "INSERT INTO passage (address, title, text) " +
            "SELECT '%s', '%s', '%s' " +
            "WHERE NOT EXISTS (SELECT 1 FROM passage WHERE address='%s' AND title='%s');",
            row[4], row[5], row[6], row[4], row[5]  // Ensure uniqueness based on BOTH address and title
        );
        sqlStatements.add(passageSql);

        // Insert Skill (if not exists)
        String skillSql = String.format(
            "INSERT INTO skill (name, category, learning_stage, course_id) " +
            "SELECT '%s', '%s', '%s', (SELECT id FROM course WHERE name='Reading') " +
            "WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name='%s');",
            row[1], row[2], row[3], row[1]  // Ensure uniqueness based on skill name
        );
        sqlStatements.add(skillSql);

        // Generate Question Metadata (MCQ or SEQUENCING)
        String questionMetadata = generateQuestionMetadata(row);

        // Insert Question
        String questionSql = String.format(
            "INSERT INTO question (text, question_type, difficulty, metadata, skill_id, passage_id) " +
            "VALUES ('%s', '%s', '%s', '%s', " +
            "(SELECT id FROM skill WHERE name='%s'), " +
            "(SELECT id FROM passage WHERE address='%s' AND title='%s'));",  // Select passage by both address & title
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
	
}
