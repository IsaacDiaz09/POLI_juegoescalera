package dev.poli.students.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Game {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GameTurn {
        private int consecutive;
        private Player player;
        private String questionText;
        private String answerText;
    }

    private final List<Player> players;
    private final List<GameTurn> playedTurns = new ArrayList<>();
    private final GameConfiguration configuration;
    private final Instant startTime;
    private Instant endTime;
    private Player winner;

    @Builder
    public Game(List<Player> players, GameConfiguration configuration) {
        this.players = players;
        this.configuration = configuration;
        this.startTime = Instant.now();
    }

    public void adddTurn(String playerId, String question, String answer) {
        int consecutive = this.playedTurns.size() + 1;
        this.playedTurns.add(GameTurn.builder()
                .consecutive(consecutive)
                .questionText(question)
                .answerText(answer)
                .build());
    }

    public void declareWinner(@NonNull Player winner) {
        if (this.winner == null) {
            this.endTime = Instant.now();
            this.winner = winner;
        } else {
            throw new IllegalStateException("Player [" + this.winner.getName() + "] has already won this game");
        }
    }

    public void nextTurn(String playerId) {
        findPlayer(playerId).ifPresent(Player::nextTurn);
    }

    public void incrementOkAnsweredQuestion(String playerId) {
        findPlayer(playerId).ifPresent(Player::okAnsweredQuestion);
    }

    public Optional<Player> findPlayer(String playerId) {
        return this.players.stream()
                .filter(p -> p.getName().equals(playerId))
                .findFirst();
    }
}
