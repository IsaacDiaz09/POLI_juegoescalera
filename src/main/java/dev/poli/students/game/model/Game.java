package dev.poli.students.game.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    public static class GameTurn {
        private final int consecutive;
        private final Player player;
        private final String questionText;
        private final String answerText;

        public GameTurn(int consecutive, Player player, String questionText, String answerText) {
            this.consecutive = consecutive;
            this.player = player;
            this.questionText = questionText;
            this.answerText = answerText;
        }

        public String getAnswerText() {
            return answerText;
        }

        public String getQuestionText() {
            return questionText;
        }

        public Player getPlayer() {
            return player;
        }

        public int getConsecutive() {
            return consecutive;
        }
    }

    private final List<Player> players;
    private final List<GameTurn> playedTurns = new ArrayList<>();
    private final GameConfiguration configuration;
    private final Instant startTime;
    private Instant endTime;
    private Player winner;

    public Game(List<Player> players, GameConfiguration configuration) {
        this.players = Collections.unmodifiableList(players);
        this.configuration = configuration;
        this.startTime = Instant.now();
    }

    public void adddTurn(String playerId, String question, String answer) {
        int consecutive = this.playedTurns.size() + 1;
        this.playedTurns.add(new GameTurn(consecutive, findPlayer(playerId), question, answer));
    }

    public void declareWinner(Player winner) {
        if (this.winner == null) {
            this.endTime = Instant.now();
            this.winner = winner;
        } else {
            throw new IllegalStateException("Player [" + this.winner.getName() + "] has already won this game");
        }
    }

    public Player findPlayer(String playerId) {
        return this.players.stream()
                .filter(p -> p.getName().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<GameTurn> getPlayedTurns() {
        return playedTurns;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Player getWinner() {
        return winner;
    }
}
