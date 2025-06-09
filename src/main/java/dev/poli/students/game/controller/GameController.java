package dev.poli.students.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.poli.students.game.Main;
import dev.poli.students.game.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameController {

    @FXML
    public ImageView mapImage;

    @FXML
    public Label resultsLabel;

    @FXML
    public Button addPlayer;

    @FXML
    public Text playersList;

    @FXML
    private Button gameStart;

    private static final Logger logger
            = LoggerFactory.getLogger(GameController.class);

    public void doStartGame() throws IOException {
        gameStart.setVisible(false);
        gameStart.setDisable(true);

        addPlayer.setVisible(false);
        addPlayer.setDisable(true);

        initializeApplicationContext();
        startGame();
    }

    public void openNewWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add-player.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 150);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Nuevo jugador");
        stage.setScene(scene);
        stage.setOnHidden((event) -> doRenderPlayersList());
        stage.show();
    }

    private void doRenderPlayersList() {
        if (PlayerController.PLAYERS.size() == 4) {
            addPlayer.setVisible(false);
            addPlayer.setDisable(true);
        }
        if (!PlayerController.PLAYERS.isEmpty()) {
            logger.info("doRenderPlayersList()");
            gameStart.setVisible(true);
            gameStart.setDisable(false);

            StringBuilder sb = new StringBuilder("Lista de jugadores: \n");
            for (Player player : PlayerController.PLAYERS) {
                sb.append("- ");
                sb.append(player.getName());
                sb.append("\n");
            }
            logger.info("label={}", sb);
            playersList.setText(sb.toString());
        }
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

        // load questions
        Path questions1Path = Paths.get("src/main/resources/config/questions.easy.json");
        Questions easyQuestions = mapper.readValue(Files.readAllBytes(questions1Path), Questions.class);

        ApplicationContext.addBean(Questions.EASY, easyQuestions);
        ApplicationContext.addBean(new TurnManager(PlayerController.PLAYERS));
    }

    private void startGame() {
        // show question dialog
        BorderPane root = new BorderPane();

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Pregunta");

        final WebView webView = new WebView();
        webView.setVisible(true);
        webView.getEngine().loadContent("<p>¿Cuánto es 5 + 3?</p>");

        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setPadding(new Insets(15, 15, 15, 15));

        for (int i = 0; i < 4; i++) {
            Button button = new Button("button-" + i);
            button.setText("Button " + i);
            button.setVisible(true);
            button.setOnAction(this::questionAnswered);
            buttonsContainer.getChildren().add(button);
        }

        webView.setMaxHeight(150);
        webView.setMaxWidth(350);

        root.setTop(webView);
        root.setBottom(buttonsContainer);

        Scene scene = new Scene(root, 350, 300);
        stage.setTitle("Pregunta");
        stage.setScene(scene);
        stage.show();
    }

    private void questionAnswered(ActionEvent event) {
        Button source = (Button) event.getSource();
        System.out.println();
    }

    private Question getQuestion() {
        Questions questions = ApplicationContext.getBean(Questions.EASY, Questions.class);
        return questions.getRandomQuestion();
    }

}