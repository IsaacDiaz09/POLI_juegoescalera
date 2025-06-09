package dev.poli.students.game.model;

import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Player {
    private final String name;
    private final Color color;
    private int currentTurn = 0;
    private int correctlyAnsweredQuestions = 0;

    @Builder
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    void nextTurn() {
        this.currentTurn++;
    }

    void okAnsweredQuestion() {
        this.correctlyAnsweredQuestions++;
    }
}
