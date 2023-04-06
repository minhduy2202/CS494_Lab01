package utils;

import java.util.Map;

public class Question extends Object {
    Integer id;
    String question = null;
    Map<String, String> answers = null;

    String solution;

    public Question(Integer id, String question, Map<String, String> answers, String solution) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.solution = solution;
    }

    public static Question fromMap(Map<String, Object> data) {
        Question question = new Question(
                Integer.parseInt((String) data.get("id")),
                (String) data.get("question"),
                (Map<String, String>) data.get("answers"),
                (String) data.get("solution"));

        return question;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Boolean isCorrect(String ans) {
        return this.solution.equals(ans);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                ", solution='" + solution + '\'' +
                '}';
    }
}
