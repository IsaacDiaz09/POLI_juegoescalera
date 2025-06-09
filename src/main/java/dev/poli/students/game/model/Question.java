package dev.poli.students.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Question {

    private String textValue;
    private List<Answer> answers;

    @Getter
    @AllArgsConstructor
    static class Answer {
        private String textValue;
        private boolean valid;
    }
}
