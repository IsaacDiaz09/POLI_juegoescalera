package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private Map<String, String> answers;

    @JsonProperty("correct")
    private String correctAnswer;
}
