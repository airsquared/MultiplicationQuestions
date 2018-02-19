package multiplication;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ResultsController {

    @FXML private TableView<SavedData> table;

    @FXML private TableColumn date;
    @FXML private TableColumn total;
    @FXML private TableColumn wrong;
    @FXML private TableColumn right;
    @FXML private TableColumn duration;
    private static Stage primaryStage;


    public static void showScene(Stage primaryStage) {
        primaryStage.setTitle("Multiplication Results");
        Parent root = null;
        try {
            root = FXMLLoader.load(ResultsController.class.getResource("results.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Scene scene = new Scene(root, 850, 500);
        scene.getStylesheets().add(QuestionController.class.getResource("app.css").toExternalForm());
        primaryStage.setScene(scene);
        ResultsController.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        showTable();
    }

    private final ObservableList<SavedData> dataList = FXCollections.observableArrayList();

    private void showTable() {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        wrong.setCellValueFactory(new PropertyValueFactory<>("wrong"));
        right.setCellValueFactory(new PropertyValueFactory<>("right"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        table.setItems(dataList);

        final String csvFile = "MultiplicationResults.csv";
        final String fieldDelimiter = ",";


        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            int whileCounter = 0;
            while (((line = br.readLine()) != null)) {
                if (whileCounter > 0) {
                    String[] fields = line.split(fieldDelimiter, -1);
                    SavedData savedData = new SavedData(fields[0], fields[1], fields[2], fields[3], fields[4]);
                    dataList.add(savedData);
                } else {
                    whileCounter = 1;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.setEditable(false);
    }

    public void mainMenu() {
        ChooserController.showScene(primaryStage);
    }

    public class SavedData {
        private SimpleStringProperty date, total, wrong, right, duration;

        public SavedData(String date, String total, String wrong, String right, String duration) {
            this.date = new SimpleStringProperty(date);
            this.total = new SimpleStringProperty(total);
            this.wrong = new SimpleStringProperty(wrong);
            this.right = new SimpleStringProperty(right);
            this.duration = new SimpleStringProperty(duration);
        }

        public String getRight() {
            return right.get();
        }


        public String getDate() {
            return date.get();
        }


        public String getTotal() {
            return total.get();
        }


        public String getWrong() {
            return wrong.get();
        }


        public String getDuration() {
            return duration.get();
        }

    }
}
