package multiplication;

import javafx.application.Application;
import javafx.stage.Stage;

public class Multiply extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ChooserController.showScene(primaryStage);
    }
}