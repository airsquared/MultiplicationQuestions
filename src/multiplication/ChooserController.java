package multiplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooserController {

    @FXML private TextField numQuestions;

    private static final int LIMIT = 3;

    @FXML
    public void initialize() {
        limitCharacters();
        intOnly();
    }

    private void limitCharacters() {
        numQuestions.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (numQuestions.getText().length() >= LIMIT) {
                    numQuestions.setText(numQuestions.getText().substring(0, LIMIT));
                }
            }
        });
    }

    private void intOnly() {
        numQuestions.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numQuestions.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    static void showScene(Stage primaryStage) {
        primaryStage.setTitle("Multiplication");
        Parent root = null;
        try {
            root = FXMLLoader.load(ChooserController.class.getResource("chooser.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Scene scene = new Scene(root, 850, 500);
        scene.getStylesheets().add(ChooserController.class.getResource("app.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    private void showQuestionScene(int tablesOf) {
        QuestionController.showScene((Stage) numQuestions.getScene().getWindow(), tablesOf, Integer.valueOf(numQuestions.getText()));
    }

    @FXML private Button prevResults;

    public void prevResults() {
        ResultsController.showScene(((Stage) numQuestions.getScene().getWindow()));
    }

    public void tableClickedHandler(ActionEvent evt) {
        Button button = (Button) evt.getTarget();
        String buttonText = button.getText();
        if (buttonText.equals("All(1-12)")) {
            showQuestionScene(0);
        } else if (buttonText.equals("1's Table") || buttonText.equals("Too bad")) {
            button.setText("Too bad");
        } else {
            String index = buttonText.substring(0, (buttonText.indexOf(" Tables")));
            int table = Integer.valueOf(index);
            showQuestionScene(table);
        }
    }
}