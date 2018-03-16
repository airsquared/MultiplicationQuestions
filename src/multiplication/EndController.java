package multiplication;


import com.google.common.base.Stopwatch;
import com.opencsv.CSVWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EndController {

    private static int numQuestions;
    private static int qCorrect;
    private static int qWrong;
    private static Stopwatch stopwatch;
    private static String timeTaken;
    private static Stage primaryStage;

    static void showScene(int numQuestions, int qCorrect, Stopwatch stopwatch, Stage primaryStage) {
        EndController.numQuestions = numQuestions;
        EndController.qCorrect = qCorrect;
        EndController.stopwatch = stopwatch;
        qWrong = numQuestions - qCorrect;
        primaryStage.setTitle("Multiplication Results");
        Parent root = null;
        try {
            root = FXMLLoader.load(EndController.class.getResource("end.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Scene scene = new Scene(root, 850, 500);
        scene.getStylesheets().add(QuestionController.class.getResource("app.css").toExternalForm());
        primaryStage.setScene(scene);
        EndController.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        showStats();
    }

    @FXML private Label numQLabel;
    @FXML private Label qCorrectLabel;
    @FXML private Label qWrongLabel;
    @FXML private Label title;
    @FXML private Label timeTakenLabel;

    private void showStats() {
        if (qCorrect == numQuestions) {
            title.setText("Great Job, 100%");
        }

        numQLabel.setText(Integer.toString(numQuestions));
        qCorrectLabel.setText(Integer.toString(qCorrect));
        qWrongLabel.setText(Integer.toString(qWrong));

        int stopwatchMin = (int) stopwatch.elapsed(TimeUnit.MINUTES);
        long stopwatchSec = stopwatch.elapsed(TimeUnit.SECONDS) - stopwatchMin * 60;
        if (stopwatchMin > 0) {
            if (stopwatchMin == 1) {
                timeTakenLabel.setText(stopwatchMin + " Minute and " + stopwatchSec + " Seconds");
            } else {
                timeTakenLabel.setText(stopwatchMin + " Minutes and " + stopwatchSec + " Seconds");
            }
        } else {
            timeTakenLabel.setText(stopwatch.toString());
        }
        timeTaken = String.format("%02d:%02d", stopwatchMin, stopwatchSec);
        try {
            saveStats();
            System.out.println("Saved Stats to MultiplicationResults.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStats() throws IOException {
        final String csv = "MultiplicationResults.csv";
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        String dateString = dateFormat.format(new Date());
        String[] newStats = new String[]{dateString, String.valueOf(numQuestions), String.valueOf(qWrong), String.valueOf(qCorrect), timeTaken};
        final String[] header = new String[]{"Date", "Total", "Wrong", "Right", "Duration"};
        Boolean fileExists = new File(csv).exists();
        Boolean headerExists = false;

        if (fileExists) { // sets the headerExists variable
            headerExists = "\"Date\",\"Total\",\"Wrong\",\"Right\",\"Duration\"".equals(new BufferedReader(new FileReader(csv)).readLine());
        }

        if (headerExists) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(csv, true))) {
                writer.writeNext(newStats);
            }
        } else if (fileExists) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(csv, true))) {
                writer.writeNext(header);
                writer.writeNext(newStats);
            }
        } else {
            try (CSVWriter writer = new CSVWriter(new FileWriter(csv))) {
                writer.writeNext(header);
                writer.writeNext(newStats);
            }
        }
    }

    public void mainMenu() {
        ChooserController.showScene(primaryStage);
    }
}