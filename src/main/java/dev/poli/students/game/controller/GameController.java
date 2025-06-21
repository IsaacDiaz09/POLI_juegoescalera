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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class GameController {

    @FXML
    public ImageView mapImage;
    @FXML
    public Button addPlayer;
    @FXML
    public WebView webView;
    @FXML
    public Circle player1Circle;
    @FXML
    public Circle player2Circle;
    @FXML
    public Circle player3Circle;
    @FXML
    public Circle player4Circle;
    @FXML
    public Text resultsLabel;
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
        displayQuestion();
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

            printResults(null);
        }
    }

    private void printResults(Player playerInTurn) {
        StringBuilder sb = new StringBuilder("Lista de jugadores: \n");
        if (playerInTurn == null) {
            sb.append("El juego no ha iniciado todavia.");
            for (Player player : PlayerController.PLAYERS) {
                sb.append("\n")
                        .append("- ")
                        .append(player.getName());
            }
        } else {
            sb.append("Juego en curso. Turno no. ")
                    .append(playerInTurn.getCurrentTurn())
                    .append(" de ")
                    .append(playerInTurn.getName());

            for (Player player : PlayerController.PLAYERS) {
                sb.append("\n")
                        .append("- ")
                        .append(player.getName())
                        .append(" - Puntuacion: ")
                        .append(player.getCorrectlyAnsweredQuestions())
                        .append("\n");
            }
        }

        resultsLabel.setText(sb.toString());
    }

    private void initializeApplicationContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path configuration = Paths.get("src/main/resources/config/maps.config.json");
        MapsConfiguration gameMaps = mapper.readValue(Files.readAllBytes(configuration), MapsConfiguration.class);
        ApplicationContext.addBean(gameMaps);

        GameConfiguration gameConfig = new GameConfiguration(gameMaps.defaultMap(), gameMaps);

        Game game = new Game(PlayerController.PLAYERS, gameConfig);
        ApplicationContext.addBean(game);

        // load questions
        Path easyQuestionsPath = Paths.get("src/main/resources/config/questions.easy.json");
        Questions easyQuestions = mapper.readValue(Files.readAllBytes(easyQuestionsPath), Questions.class);
        ApplicationContext.addBean(Questions.EASY, easyQuestions);

        Path mediumQuestionsPath = Paths.get("src/main/resources/config/questions.medium.json");
        Questions mediumQuestions = mapper.readValue(Files.readAllBytes(mediumQuestionsPath), Questions.class);
        ApplicationContext.addBean(Questions.MEDIUM, mediumQuestions);

        Path hardQuestionsPath = Paths.get("src/main/resources/config/questions.hard.json");
        Questions hardQuestions = mapper.readValue(Files.readAllBytes(hardQuestionsPath), Questions.class);
        ApplicationContext.addBean(Questions.HARD, hardQuestions);

        TurnManager turnManager = new TurnManager(PlayerController.players(player1Circle, player2Circle,
                player3Circle, player4Circle));
        ApplicationContext.addBean(turnManager);
    }

    private void displayQuestion() {
        // show question dialog
        BorderPane root = new BorderPane();

        Stage stage = new Stage();
        stage.setResizable(false);

        final WebView webView = new WebView();
        webView.setVisible(true);

        Question question = ApplicationContext.getBean(Questions.EASY, Questions.class)
                .getRandomQuestion();

        TurnManager turnManager = ApplicationContext.getBean(TurnManager.class);
        Pair<Player, Circle> p = turnManager.nextTurn();
        Player player = p.getLeft();
        printResults(player);

        // do only in the first turn
        if (player.getCurrentTurn() == 1) {
            p.getRight().setVisible(true);
            p.getRight().setFill(player.getColor());
        }

        StringBuilder questionContent = new StringBuilder(question.getQuestion());
        for (Map.Entry<String, String> answer : question.getAnswers().entrySet()) {
            String answerContent = String.format("<p>%s.) %s</p>", answer.getKey(), answer.getValue());
            questionContent.append(answerContent);
        }
        webView.getEngine().loadContent(questionContent.toString());

        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setPadding(new Insets(15, 15, 15, 15));

        for (Map.Entry<String, String> answer : question.getAnswers().entrySet()) {
            Button button = new Button("button-" + Objects.hash(question.getQuestion(), answer.getValue()));
            button.setText(answer.getKey());
            button.setVisible(true);
            button.setOnAction((e) -> questionAnswered(e, turnManager, question));
            buttonsContainer.getChildren().add(button);
        }

        webView.setMaxHeight(250);
        webView.setMaxWidth(350);

        root.setTop(webView);
        root.setBottom(buttonsContainer);

        Scene scene = new Scene(root, 350, 300);
        stage.setScene(scene);
        String title = String.format("Turno no. %s de %s", player.getCurrentTurn(), player.getName());
        stage.setOnCloseRequest(e -> logger.info("Question has been closed"));
        stage.setTitle(title);
        stage.show();
    }

    private void questionAnswered(ActionEvent event, TurnManager turnManager, Question question) {
        Button source = (Button) event.getSource();
        String answer = source.getText();
        turnManager.incrementOkAnsweredQuestions();

        if (question.getCorrectAnswer().equals(answer)) {
            logger.info("La respuesta es correcta: [{}/{}]", question.getQuestion(), answer);
        } else {
            logger.error("La respuesta es incorrecta");
        }
    }

    private Question getQuestion(Questions.Difficulty difficulty) {
        Questions questions = ApplicationContext.getBean(Questions.EASY, Questions.class);
        return questions.getRandomQuestion();
    }

}