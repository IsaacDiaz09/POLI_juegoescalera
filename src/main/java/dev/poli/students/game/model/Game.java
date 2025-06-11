package dev.poli.students.game.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una partida del juego de preguntas-respuestas.
 * <p>
 * Un {@code Game} mantiene la lista inmutable de jugadores, el histórico de turnos
 * jugados, la configuración utilizada y metadatos de inicio/fin.
 * La clase NO contiene la lógica de negocio sobre la validez de las respuestas:
 * solo registra la información para que capas superiores la procesen.
 */
public class Game {

    /**
     * Estructura inmutable que describe un turno ya jugado.
     * Cada turno almacena su número consecutivo (1-based), el jugador que lo
     * realizó y la dupla pregunta-respuesta mostrada al usuario.
     */
    public static class GameTurn {

        private final int consecutive;
        private final Player player;
        private final String questionText;
        private final String answerText;

        /**
         * Crea un turno jugado.
         *
         * @param consecutive   ordinal del turno dentro de la partida (empieza en 1)
         * @param player        jugador que respondió en este turno
         * @param questionText  texto completo de la pregunta presentada
         * @param answerText    texto de la respuesta introducida por el jugador
         */
        public GameTurn(int consecutive,
                        Player player,
                        String questionText,
                        String answerText) {
            this.consecutive = consecutive;
            this.player      = player;
            this.questionText = questionText;
            this.answerText   = answerText;
        }

        /** @return respuesta introducida por el jugador */
        public String getAnswerText()   { return answerText; }

        /** @return pregunta presentada en el turno */
        public String getQuestionText() { return questionText; }

        /** @return jugador que participó en el turno */
        public Player getPlayer()       { return player; }

        /** @return número consecutivo (1-based) del turno */
        public int getConsecutive()     { return consecutive; }
    }

    /* --------------------------------- Atributos -------------------------------- */

    private final List<Player> players;
    private final List<GameTurn> playedTurns = new ArrayList<>();
    private final GameConfiguration configuration;

    private final Instant startTime;
    private Instant endTime;
    private Player winner;

    /* -------------------------------- Constructores ----------------------------- */

    /**
     * Crea una nueva partida.
     *
     * @param players        lista de jugadores participantes.
     *                       Se envuelve en {@code Collections.unmodifiableList}
     *                       para evitar modificaciones externas.
     * @param configuration  configuración empleada (mapas, reglas, etc.).
     */
    public Game(List<Player> players, GameConfiguration configuration) {
        this.players       = Collections.unmodifiableList(players);
        this.configuration = configuration;
        this.startTime     = Instant.now();
    }

    /* ------------------------------ Lógica de dominio --------------------------- */

    /**
     * Registra un nuevo turno jugado.
     *
     * @param playerId identificador único (nombre) del jugador que responde.
     * @param question texto de la pregunta mostrada.
     * @param answer   respuesta ingresada por el jugador.
     * @throws IllegalArgumentException si el jugador no existe.
     */
    public void adddTurn(String playerId, String question, String answer) {
        int consecutive = this.playedTurns.size() + 1;
        this.playedTurns.add(
                new GameTurn(consecutive, findPlayer(playerId), question, answer)
        );
    }

    /**
     * Declara al ganador de la partida. Solo puede invocarse una vez.
     *
     * @param winner jugador que ha ganado.
     * @throws IllegalStateException si la partida ya tiene ganador.
     */
    public void declareWinner(Player winner) {
        if (this.winner == null) {
            this.endTime = Instant.now();
            this.winner  = winner;
        } else {
            throw new IllegalStateException(
                    "Player [" + this.winner.getName() + "] has already won this game"
            );
        }
    }

    /**
     * Busca un jugador por su identificador.
     *
     * @param playerId nombre/ID del jugador.
     * @return instancia {@link Player} correspondiente.
     * @throws IllegalArgumentException si el jugador no existe en la partida.
     */
    public Player findPlayer(String playerId) {
        return this.players.stream()
                .filter(p -> p.getName().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    /* ----------------------------- Getters públicos ----------------------------- */

    /** @return lista inmutable de jugadores de la partida */
    public List<Player> getPlayers()            { return players; }

    /** @return historial de turnos jugados */
    public List<GameTurn> getPlayedTurns()      { return playedTurns; }

    /** @return configuración utilizada al iniciar la partida */
    public GameConfiguration getConfiguration() { return configuration; }

    /** @return instante de inicio de la partida */
    public Instant getStartTime()               { return startTime; }

    /** @return instante de finalización (solo si hay ganador) */
    public Instant getEndTime()                 { return endTime; }

    /** @return jugador ganador o {@code null} si aún no se ha declarado */
    public Player getWinner()                   { return winner; }
}
