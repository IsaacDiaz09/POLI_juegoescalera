package dev.poli.students.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.poli.students.game.Main;
import dev.poli.students.game.model.Game;
import dev.poli.students.game.model.GameConfiguration;
import dev.poli.students.game.model.MapsConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameController {


    public ImageView mapImage;
    public Label resultsLabel;
    public Button test;
    @FXML
    private Button gameStart;

    public void doStartGame() throws IOException {
        initializeApplicationContext();
    }

    public void openNewWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add-player.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 150);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Nuevo jugador");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeApplicationContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path configuration = Paths.get("src/main/resources/config/maps.config.json");
        MapsConfiguration gameMaps = mapper.readValue(Files.readAllBytes(configuration), MapsConfiguration.class);
        ApplicationContext.addBean(gameMaps);
        GameConfiguration gameConfig = GameConfiguration.builder()
                .mapConfiguration(gameMaps.defaultMap())
                .maps(gameMaps)
                .build();
        Game game = Game.builder()
                .configuration(gameConfig)
                .players(PlayerController.PLAYERS)
                .build();
        ApplicationContext.addBean(game);
    }

}