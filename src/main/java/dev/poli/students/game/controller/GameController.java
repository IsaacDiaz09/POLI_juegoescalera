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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public StackPane pane;
    @FXML
    private Button gameStart;


    private static final String CORRECT_ANSWER = "Tu respuesta es correcta, avanzas %d casillas";
    private static final String INCORRECT_ANSWER = "Tu respuesta es incorrecta, retrocedes %d casillas";
    private static final String LADDER_TRIGGERED_MESSAGE = "Has activado un comodin de escalera, avanzas hasta la casilla %d";
    private static final String SNAKE_TRIGGERED_MESSAGE = "Has activado un comodin de serpiente, retrocedes hasta la casilla %d";


    private static final Logger logger
            = LoggerFactory.getLogger(GameController.class);

    public void doPlayTurn(Questions.Difficulty questionDifficulty) throws IOException {
        initializeApplicationContext();
        displayQuestion(questionDifficulty);
    }

    public void doChoiceQuestionDifficulty() {
        String[] values = {Questions.Difficulty.EASY.getDescription(),
                Questions.Difficulty.MEDIUM.getDescription(),
                Questions.Difficulty.HARD.getDescription()
        };

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(Questions.Difficulty.EASY.getDescription());
        choiceDialog.getItems().addAll(values);
        choiceDialog.setResizable(false);

        choiceDialog.setOnCloseRequest((e) -> {
            try {
                logger.info("se jugara una pregunta {}", choiceDialog.getSelectedItem());
                doPlayTurn(Questions.Difficulty.byName(choiceDialog.getSelectedItem()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // disable start controls when game is started
        gameStart.setVisible(false);
        gameStart.setDisable(true);
        addPlayer.setVisible(false);
        addPlayer.setDisable(true);

        choiceDialog.setTitle("Dificultad del turno");
        choiceDialog.showAndWait();
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
        if (!PlayerController.PLAYERS.isEmpty()) {
            printResults(null);
        }
        if (PlayerController.PLAYERS.size() >= 2 && PlayerController.PLAYERS.size() < 4) {
            gameStart.setVisible(true);
            gameStart.setDisable(false);
        }
    }

    private void printResults(Player playerInTurn) {
        StringBuilder sb = new StringBuilder("Lista de jugadores: \n");
        if (playerInTurn == null) {
            sb.append("El juego no ha iniciado todavia.");
            if (PlayerController.PLAYERS.size() < 2) {
                sb.append("\n")
                        .append("Se requiere minimo dos jugadores para iniciar la partida");
            }
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
        if (!ApplicationContext.isEmpty()) {
            return;
        }
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

    private void displayQuestion(Questions.Difficulty questionDifficulty) {
        // show question dialog
        BorderPane root = new BorderPane();

        Stage stage = new Stage();
        stage.setResizable(false);

        final WebView webView = new WebView();
        webView.setVisible(true);

        Question question = getRandomQuestion(questionDifficulty);

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
            button.setOnAction((e) -> questionAnswered(e, stage, turnManager, question, questionDifficulty));
            buttonsContainer.getChildren().add(button);
        }

        webView.setMaxHeight(250);
        webView.setMaxWidth(350);

        root.setTop(webView);
        root.setBottom(buttonsContainer);

        Scene scene = new Scene(root, 350, 300);
        stage.setScene(scene);
        String title = String.format("Turno no. %s de %s", player.getCurrentTurn(), player.getName());
        stage.setOnCloseRequest(e -> logger.warn("Question has been closed"));
        stage.setTitle(title);
        stage.show();
    }

    private void questionAnswered(ActionEvent event, Stage stage, TurnManager turnManager,
                                  Question question, Questions.Difficulty difficulty) {
        Button source = (Button) event.getSource();
        Player playerInTurn = turnManager.getPlayer().getLeft();
        String answer = source.getText();

        // check answer
        boolean correctAnswer = question.getCorrectAnswer().equals(answer);
        int points = correctAnswer ? difficulty.getPoints() : -difficulty.getPoints();

        // move player circle/pointer to the correct cell
        int newPosition = playerInTurn.getCurrentCell() + points;

        //int xIncrement = (newPosition % 10) - (playerInTurn.getCurrentCell() % 10);
        //int yIncrement = 0;

        //if ((newPosition / 10) > 0) {
        //    yIncrement = (newPosition / 10) - (playerInTurn.getCurrentCell() % 10);
        //}

        //double xPosition = xIncrement * 44;
        //double yPosition = yIncrement * 39;

        //Circle playerPointer = turnManager.getPlayer().getRight();

        //if (correctAnswer || playerInTurn.getCurrentTurn() > 1) {
        //    Translate translate = new Translate();
        //    translate.setX(xPosition);
        //    if (yPosition > 0) {
        //        translate.setY(yPosition);
        //    }

        //    playerPointer.getTransforms().add(translate);
        //    playerInTurn.setCurrentCell(newPosition);
        //    logger.info("updated player position to cell no. {}", newPosition);
        //}

        Circle playerPointer = turnManager.getPlayer().getRight();
        updatePlayerPositionInMap(playerInTurn, playerPointer, newPosition);

        if (question.getCorrectAnswer().equals(answer)) {
            logger.info("La respuesta es correcta: [{}/{}]", question.getQuestion(), answer);
            turnManager.incrementOkAnsweredQuestions();
            showInfoAlert(CORRECT_ANSWER, points);
        } else {
            logger.error("La respuesta es incorrecta");
            if (newPosition > 0) {
                showWarnAlert(INCORRECT_ANSWER, Math.abs(points));
            } else {
                showWarnAlert("Tu respuesta es incorrecta");
            }
        }
        // check cell comodin
        checkComodin(playerInTurn, playerPointer);

        // close the question's window
        stage.close();

        // show game summary
        printResults(playerInTurn);

        //triger next turn
        doChoiceQuestionDifficulty();
    }

    private Question getRandomQuestion(Questions.Difficulty difficulty) {
        if (difficulty == Questions.Difficulty.EASY) {
            Questions questions = ApplicationContext.getBean(Questions.EASY, Questions.class);
            return questions.getRandomQuestion();
        } else if (difficulty == Questions.Difficulty.MEDIUM) {
            Questions questions = ApplicationContext.getBean(Questions.MEDIUM, Questions.class);
            return questions.getRandomQuestion();
        } else if (difficulty == Questions.Difficulty.HARD) {
            Questions questions = ApplicationContext.getBean(Questions.HARD, Questions.class);
            return questions.getRandomQuestion();
        }
        throw new IllegalStateException("Invalid question type");
    }

    private void showWarnAlert(String message, Object... args) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Informacion");
        alert.setContentText(String.format(message, args));
        alert.showAndWait();
    }

    private void showInfoAlert(String message, Object... args) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Informacion");
        alert.setContentText(String.format(message, args));
        alert.showAndWait();
    }

    private void checkComodin(Player player, Circle pointer) {
        Game game = ApplicationContext.getBean(Game.class);
        MapConfiguration mapConfiguration = game.getConfiguration().getMapConfiguration();
        Optional<MapConfiguration.Comodin> comodin = mapConfiguration.getComodins().stream()
                .filter(c -> c.getFromPosition() == player.getCurrentCell())
                .findFirst();
        comodin.ifPresent(c -> {
            updatePlayerPositionInMap(player, pointer, c.getToPosition());
            if (c.comodinType() == MapsConfiguration.ComodinType.LADDER) {
                showInfoAlert(LADDER_TRIGGERED_MESSAGE, c.getToPosition());
            } else if (c.comodinType() == MapsConfiguration.ComodinType.SNAKE) {
                showWarnAlert(SNAKE_TRIGGERED_MESSAGE, c.getToPosition());
            }
        });
    }

    private void updatePlayerPositionInMap(Player player, Circle pointer, int newPosition) {
        int xIncrement = (newPosition % 10) - (player.getCurrentCell() % 10);
        int yIncrement = (newPosition / 10) - (player.getCurrentCell() / 10);

        double xPosition = 0;
        double yPosition = yIncrement * -39;

        if (yIncrement > 0) {
            yIncrement = yIncrement * 39;
            xIncrement = 1;
            for (Integer i: getXAxisNearestValue(newPosition)) {
                if (i == newPosition) {
                    break;
                }
                xIncrement++;
            }
            xIncrement = player.getCurrentCell() - xIncrement;
        }

        // placement over X depends on Which Y column am I
        if (yIncrement % 2 == 0) {
            xPosition = xIncrement * 44;
            logger.info("Right to Left movement");
        } else {
            xPosition = xIncrement * -44;
            logger.info("Left to Right movement");
        }

        if (newPosition >= 1 && player.getCurrentTurn() >= 1) {
            Translate translate = new Translate();
            translate.setX(xPosition);
            translate.setY(yPosition);

            pointer.getTransforms().add(translate);
            player.setCurrentCell(newPosition);
            logger.info("updated player position to cell no. {}", newPosition);
        }
    }

    private List<Integer> getXAxisNearestValue(int target) {
        List<List<Integer>> xAxisValues = new ArrayList<>();

        xAxisValues.add(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        xAxisValues.add(List.of(20, 19, 18, 17, 16, 15, 14, 13, 12, 11));
        xAxisValues.add(List.of(21, 22, 23, 24, 25, 26, 27, 28, 29, 30));
        xAxisValues.add(List.of(40, 39, 38, 37, 36, 35, 34, 33, 32, 31));
        xAxisValues.add(List.of(41, 42, 43, 44, 45, 46, 47, 48, 49, 50));
        xAxisValues.add(List.of(60, 59, 58, 57, 56, 55, 54, 53, 52, 51));
        xAxisValues.add(List.of(61, 62, 63, 64, 65, 66, 67, 68, 69, 70));
        xAxisValues.add(List.of(80, 79, 78, 77, 76, 75, 74, 73, 72, 71));
        xAxisValues.add(List.of(81, 82, 83, 84, 85, 86, 87, 88, 89, 90));
        xAxisValues.add(List.of(100, 99, 98, 97, 96, 95, 94, 93, 92, 91));


        int index = (target / 10);
        return xAxisValues.get(index);
    }

}