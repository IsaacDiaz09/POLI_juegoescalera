package dev.poli.students.game.model;
import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class GameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private GameConfiguration config;

    @BeforeEach
    void setUp() {

        player1 = new Player("player1", Color.RED);
        player2 = new Player("player2", Color.BLUE);

        MapConfiguration  mapConfig  = mock(MapConfiguration.class);
        MapsConfiguration mapsConfig = mock(MapsConfiguration.class);
        config = new GameConfiguration(mapConfig, mapsConfig);

        game = new Game(Arrays.asList(player1, player2), config);
    }
    @Test
    void shouldAddTurnCorrectly() {
        game.adddTurn("player1", "What is 2+2?", "4");

        List<Game.GameTurn> turns = game.getPlayedTurns();
        assertEquals(1, turns.size());

        Game.GameTurn turn = turns.get(0);
        assertEquals(1,             turn.getConsecutive());
        assertEquals("player1",     turn.getPlayer().getName());
        assertEquals("What is 2+2?",turn.getQuestionText());
        assertEquals("4",           turn.getAnswerText());
    }
    @Test
    void shouldDeclareWinnerOnlyOnce() {
        game.declareWinner(player1);

        assertEquals(player1, game.getWinner());
        assertNotNull(game.getEndTime());

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> game.declareWinner(player2)
        );

        assertTrue(ex.getMessage().contains("has already won"));
    }
    @Test
    void shouldFindPlayerByName() {
        Player found = game.findPlayer("player2");
        assertEquals(player2, found);
    }
    @Test
    void shouldThrowIfPlayerNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> game.findPlayer("unknown"));
    }
    @Test
    void shouldExposeBasicInfo() {
        assertEquals(2,          game.getPlayers().size());
        assertEquals(config,     game.getConfiguration());
        assertNotNull(game.getStartTime());
        assertNull(game.getWinner());
        assertNull(game.getEndTime());
    }


}
