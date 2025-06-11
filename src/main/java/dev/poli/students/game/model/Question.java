package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Question {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private Map<String, String> answers;

    @JsonProperty("correct")
    private String correctAnswer;

    public Question() {
    }

    public Question(String question, Map<String, String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
