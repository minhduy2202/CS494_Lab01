package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Loader {
    public static List<Question> loadCSV(String path) {
//        String fileName = "src/questions.csv";
        String fileName = path;
        List<Question> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }
                Map<String, Object> question = new HashMap<>();
                question.put("id", fields[0]);
                question.put("question", fields[1]);
                Map<String, String> answers = new HashMap<>();
                answers.put("a", fields[2]);
                answers.put("b", fields[3]);
                answers.put("c", fields[4]);
                answers.put("d", fields[5]);
                question.put("answers", answers);
                question.put("solution", fields[6]);
                questions.add((Question) Question.fromMap(question));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}
