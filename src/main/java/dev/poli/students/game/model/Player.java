package dev.poli.students.game.model;

import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Representa a un participante de la partida.
 * <p>
 * Cada jugador se identifica por un nombre visible en pantalla y un color
 * distintivo que se usa para renderizar su ficha en el tablero.
 * La clase también almacena su turno actual y cuántas preguntas ha
 * respondido correctamente.
 *
 * <p>Es inmutable en cuanto a identidad (nombre y color) y mutable en
 * sus contadores internos.</p>
 */
public class Player {

    /* ------------------------------------------------------------------ */
    /*                               Campos                               */
    /* ------------------------------------------------------------------ */

    /** Nombre público del jugador (debe ser único en la partida). */
    private final String name;

    /** Color asociado a la ficha del jugador en el tablero. */
    private final Color color;

    /** Número de turno actual (incrementa cada vez que el jugador responde). */
    private int currentTurn = 0;

    private int currentCell;

    /** Total de preguntas respondidas correctamente. */
    private int correctlyAnsweredQuestions = 0;

    /* ------------------------------------------------------------------ */
    /*                            Constructores                           */
    /* ------------------------------------------------------------------ */

    /**
     * Crea un nuevo jugador.
     *
     * @param name  nombre público del jugador.
     * @param color color con el que se dibujará su ficha.
     */
    public Player(String name, Color color) {
        this.name  = name;
        this.color = color;
        this.currentCell = 1;
    }

    /* ------------------------------------------------------------------ */
    /*                  Métodos de actualización de estado                */
    /* ------------------------------------------------------------------ */

    /** Incrementa en uno el contador de turnos jugados. */
    void nextTurn() {
        this.currentTurn++;
    }

    /** Incrementa en uno el número de respuestas correctas. */
    void okAnsweredQuestion() {
        this.correctlyAnsweredQuestions++;
    }

    public int getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(int currentCell) {
        this.currentCell = currentCell;
    }
    /* ------------------------------------------------------------------ */
    /*                                Getters                             */
    /* ------------------------------------------------------------------ */

    /** @return total de respuestas correctas del jugador. */
    public int getCorrectlyAnsweredQuestions() { return correctlyAnsweredQuestions; }

    /** @return número de turno en el que se encuentra el jugador. */
    public int getCurrentTurn()               { return currentTurn; }

    /** @return color asociado al jugador. */
    public Color getColor()                   { return color; }

    /** @return nombre público del jugador. */
    public String getName()                   { return name; }

    /* ------------------------------------------------------------------ */
    /*                  equals / hashCode (basados en estado)             */
    /* ------------------------------------------------------------------ */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return currentTurn == player.currentTurn &&
                correctlyAnsweredQuestions == player.correctlyAnsweredQuestions &&
                Objects.equals(name,  player.name) &&
                Objects.equals(color, player.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, currentTurn, correctlyAnsweredQuestions);
    }
}
