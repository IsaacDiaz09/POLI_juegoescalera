package dev.poli.students.game.model;

import javafx.scene.paint.Color;

import java.util.Objects;

public class Player {
    private final String name;
    private final Color color;
    private int currentTurn = 0;
    private int correctlyAnsweredQuestions = 0;

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

    public int getCorrectlyAnsweredQuestions() {
        return correctlyAnsweredQuestions;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return currentTurn == player.currentTurn && correctlyAnsweredQuestions == player.correctlyAnsweredQuestions && Objects.equals(name, player.name) && Objects.equals(color, player.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, currentTurn, correctlyAnsweredQuestions);
    }
}
