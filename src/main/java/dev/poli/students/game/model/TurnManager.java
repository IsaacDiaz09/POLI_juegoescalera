package dev.poli.students.game.model;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class TurnManager {
    @Getter
    private Player player;
    private final List<String> state;
    private final List<Player> players;

    public TurnManager(List<Player> players) {
        if (players.isEmpty()) {
            throw new IllegalArgumentException("No players found");
        }
        this.players = players;
        this.state = this.players.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public Player nextTurn() {
        if (state.size() == this.players.size()) {
            this.state.clear();
        }
        Player p = players.get(state.size());
        state.add(p.getName());
        p.nextTurn();
        this.player = p;
        return p;
    }

    public void incrementOkAnsweredQuestions() {
        this.player.okAnsweredQuestion();
    }

}
