package multiplication;


import com.google.common.base.Stopwatch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionController {

    private static Stage primaryStage;

    private static int tablesOf;
    private static int numQuestions;
    private volatile int factorX;
    private volatile int factorY;

    private volatile int counter;
    private int qCorrect = 0;
    private Stopwatch stopwatch;

    @FXML private ProgressBar progressBar;

    @FXML private TextField answer;
    @FXML private Label question;
    @FXML private Label qCompletedLabel;
    @FXML private Label clock;
    @FXML private Button nextButton;

    private SimpleStringProperty elapsedTime = new SimpleStringProperty();

    @FXML
    public void initialize() {
        System.out.println();
        stopwatch = Stopwatch.createStarted();
        counter = 0;
        showQuestion();
        clock.textProperty().bind(elapsedTime);
        limitCharacters();
        intOnly();
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(QuestionController.this::updateClock);
            }
        }, 0, 100);
    }

    private void updateClock() {
        if ((stopwatch.elapsed().toMillis() / 1000) / 60 > 0) {
            long min = (stopwatch.elapsed().toMillis() / 1000) / 60;
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            double sec = Double.valueOf(df.format((((stopwatch.elapsed().toMillis() / 1000.0) / 60.0) - min) * 60.0));

            elapsedTime.set(min + " min and " + sec + " s");
        } else {
            elapsedTime.set(stopwatch.toString());
        }
    }

    private void limitCharacters() {
        final int LIMIT = 3;
        answer.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (answer.getText().length() >= LIMIT) {
                    answer.setText(answer.getText().substring(0, LIMIT));
                }
            }
        });
    }

    private void intOnly() {
        answer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                answer.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void showQuestion() {
        counter = counter + 1;
        qCompletedLabel.setText("Question " + counter + " of " + numQuestions + "                       Progress:");
        answer.setText(null);
        nextButton.setDisable(false);
        if (counter < numQuestions + 1) {
            if (tablesOf > 0) {
                factorX = ThreadLocalRandom.current().nextInt(tablesOf, tablesOf + 1);
            } else if (tablesOf == 0) {
                factorX = ThreadLocalRandom.current().nextInt(2, 13);
            }
            factorY = ThreadLocalRandom.current().nextInt(2, 13);
            question.setText(factorX + "X" + factorY + "=");
        } else {
            System.out.println("stuff");
        }
    }

    public void nextClicked() {
        if (answer.getText() == null) {
            return;
        }
        if (Integer.valueOf(answer.getText()) == factorX * factorY) {
            answer.setStyle("-fx-background-color:green;");
            qCorrect = qCorrect + 1;
        } else {
            answer.setStyle("-fx-background-color:red;");
        }
        // Prevents user from getting multiple questions correct by spamming enter while animation is playing
        nextButton.setDisable(true);
        updateProgressBar();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(QuestionController.this::afterTimer);
            }
        }, 500);
    }

    private void afterTimer() {
        answer.setStyle(null);
        if (counter >= numQuestions) {
            stopwatch.stop();
            EndController.showScene(numQuestions, qCorrect, stopwatch, primaryStage);
        } else {
            showQuestion();
        }
    }

    private void updateProgressBar() {
        progressBar.setProgress((double) counter / (double) numQuestions);
    }

    static void showScene(Stage primaryStage, int tablesOf, int numQuestions) {
        QuestionController.tablesOf = tablesOf;
        QuestionController.numQuestions = numQuestions;
        QuestionController.primaryStage = primaryStage;
        primaryStage.setTitle("Multiplication Questions");
        Parent root = null;
        try {
            root = FXMLLoader.load(QuestionController.class.getResource("question.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Scene scene = new Scene(root, 850, 500);
        scene.getStylesheets().add(QuestionController.class.getResource("app.css").toExternalForm());
        primaryStage.setScene(scene);
    }


}