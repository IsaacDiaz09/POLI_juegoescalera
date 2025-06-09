package dev.poli.students.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.poli.students.game.controller.ApplicationContext;
import dev.poli.students.game.model.Game;
import dev.poli.students.game.model.GameConfiguration;
import dev.poli.students.game.model.MapsConfiguration;
import dev.poli.students.game.model.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final Logger logger
            = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting game application...");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 600);
        stage.setTitle("Juego escalera matematica!");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}