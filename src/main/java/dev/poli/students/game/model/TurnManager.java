package dev.poli.students.game.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Administra el orden de los turnos durante la partida.
 * <p>
 * La lógica es sencilla: recorre la lista de jugadores en orden y,
 * una vez que todos han jugado, reinicia el ciclo.
 * Internamente mantiene la lista {@code state} con los nombres de
 * los jugadores que ya han participado en la ronda actual.
 */
public class TurnManager {

    /** Jugador cuyo turno está en curso (último entregado por {@link #nextTurn()}). */
    private Player player;

    /** Registra los nombres de los jugadores que ya han jugado en la ronda. */
    private final List<String> state;

    /** Lista inmutable de todos los jugadores de la partida. */
    private final List<Player> players;

    /* ------------------------------------------------------------------ */
    /*                            Constructor                             */
    /* ------------------------------------------------------------------ */

    /**
     * Crea un gestor de turnos con la lista de jugadores dada.
     *
     * @param players lista de jugadores (no puede estar vacía).
     * @throws IllegalArgumentException si la lista está vacía.
     */
    public TurnManager(List<Player> players) {
        if (players.isEmpty()) {
            throw new IllegalArgumentException("No players found");
        }
        this.players = players;
        this.state   = this.players.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /* ------------------------------------------------------------------ */
    /*                               Lógica                               */
    /* ------------------------------------------------------------------ */

    /**
     * Devuelve el siguiente jugador que debe tomar turno.
     * <p>
     * Si todos los jugadores ya han jugado en la ronda actual, reinicia
     * el ciclo y comienza de nuevo desde el primero.
     *
     * @return jugador al que le toca el turno.
     */
    public Player nextTurn() {
        // Si la ronda terminó, limpiamos la bitácora de turnos
        if (state.size() == this.players.size()) {
            this.state.clear();
        }

        // El siguiente jugador es el que corresponde al tamaño actual del state
        Player p = players.get(state.size());
        state.add(p.getName()); // registramos que ya jugó
        p.nextTurn();           // actualizamos su contador interno
        this.player = p;        // lo marcamos como jugador actual
        return p;
    }

    /**
     * Incrementa el conteo de respuestas correctas del jugador
     * que está en turno (el último devuelto por {@link #nextTurn()}).
     */
    public void incrementOkAnsweredQuestions() {
        this.player.okAnsweredQuestion();
    }

    /* ------------------------------------------------------------------ */
    /*                                Getter                              */
    /* ------------------------------------------------------------------ */

    /**
     * Devuelve el jugador que tiene actualmente el turno.
     *
     * @return jugador en turno, o {@code null} si aún no se ha invocado
     *         {@link #nextTurn()}.
     */
    public Player getPlayer() {
        return player;
    }
}
