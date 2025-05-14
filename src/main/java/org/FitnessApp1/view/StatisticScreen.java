package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.CalorieLog;
import org.FitnessApp1.model.CalorieLogDAO;
import org.FitnessApp1.model.SessionManager;

import java.time.LocalDate;
import java.util.List;

public class StatisticScreen {

    private VBox layout;
    private Button backButton;
    private LineChart<Number, Number> calorieChart;
    private BorderPane root;

    public StatisticScreen() {
        layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Skapa X- och Y-axlar för grafen
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Dagar");
        yAxis.setLabel("Kalorier");

        // Skapa linjediagrammet
        calorieChart = new LineChart<>(xAxis, yAxis);
        calorieChart.setTitle("Kalorier per dag");

        // Visa data
        showCalorieData();

        Image homeImage = new Image(getClass().getResourceAsStream("/images/home.png"));
        ImageView homeIcon = new ImageView(homeImage);
        homeIcon.setFitWidth(24);
        homeIcon.setFitHeight(24);
        homeIcon.setPreserveRatio(true);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> {
            MainMenuScreen menuScreen = new MainMenuScreen(SessionManager.getUsername());
            new MainMenuController(menuScreen, Main.getPrimaryStage());
            Main.getPrimaryStage().setScene(new Scene(menuScreen.getRoot(), 800, 600));
        });

// Layout
//        layout.getChildren().add(calorieChart);

        root = new BorderPane();
        root.setCenter(layout);

        HBox bottomBox = new HBox(homeIcon);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setStyle("-fx-alignment: center;");
        bottomBox.setPadding(new Insets(60, 0, 60, 0));
        root.setBottom(bottomBox);
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));

        Label homeLabel = new Label("↩ Hem");
        homeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        homeLabel.setOnMouseClicked(homeIcon.getOnMouseClicked()); // klickbar text också

        VBox homeBox = new VBox(5, homeIcon, homeLabel);
        homeBox.setAlignment(Pos.CENTER);
        homeBox.setPadding(new Insets(10, 0, 0, 0));


        layout.getChildren().addAll(calorieChart, homeBox);
        root.setCenter(layout);

    }

    public Parent getRoot() {
        return root;
    }

    public void showInPrimaryWindow() {
        StatisticScreen statScreen = new StatisticScreen();
        Scene statScene = new Scene(statScreen.getRoot(), 800, 600);
        Main.getPrimaryStage().setScene(statScene);

    }

    private void showCalorieData() {
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();

        CalorieLogDAO dao = new CalorieLogDAO();
        List<CalorieLog> logs = dao.getLogsForDateRange(startDate, endDate, kontoID);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Kalorier");

        for (CalorieLog logg : logs) {
            int days = logg.getDatum().getDayOfMonth();
            series.getData().add(new XYChart.Data<>(days, logg.getKalorier()));
        }

        calorieChart.getData().add(series);
    }
}
