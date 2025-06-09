package dev.poli.students.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Questions {

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
    }

    private Difficulty difficulty;
    private List<Question> questions;
}
