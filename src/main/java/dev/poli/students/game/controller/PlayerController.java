package dev.poli.students.game.controller;

import dev.poli.students.game.model.Pair;
import dev.poli.students.game.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerController {

    public static final List<Player> PLAYERS = new ArrayList<>();

    @FXML
    private ColorPicker playerColorPicker;

    @FXML
    private TextField playerNameTextField;

    @FXML
    private Button addPlayer;

    public void addPlayer() {
        Stage stage = (Stage) addPlayer.getScene().getWindow();

        String playerName = playerNameTextField.getText();
        Color playerColor = playerColorPicker.getValue();

        PLAYERS.add(new Player(playerName, playerColor));

        stage.close();
    }

    public static List<Pair<Player, Circle>> players(Circle... playerCircles) {
        int i = 0;
        List<Pair<Player, Circle>> state = new ArrayList<>();
        for (Player player : PLAYERS) {
            state.add(Pair.of(player, playerCircles[i]));
            i++;
        }
        return Collections.unmodifiableList(state);
    }
}
