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
import java.util.Map;
import java.util.TreeMap;

public class StatisticScreen {

    private VBox layout;
    private Button backButton;
    private LineChart<Number, Number> calorieChart;
    private BorderPane root;

    public StatisticScreen() {
        layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Dynamiskt intervall beroende på antal dagar i månaden
        int maxDaysInMonth = LocalDate.now().lengthOfMonth();

        // Skapa X- och Y-axlar med fasta heltal från 1 till 30/31
        NumberAxis xAxis = new NumberAxis(1, maxDaysInMonth, 1);
        xAxis.setLabel("Dagar");
        xAxis.setTickUnit(1);
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%.0f", object.doubleValue());
            }
        });

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Kalorier");

        // Skapa linjediagrammet
        calorieChart = new LineChart<>(xAxis, yAxis);
        calorieChart.setTitle("Kalorier per dag");
        calorieChart.setCreateSymbols(false); // Ta bort punkter, visa endast linje

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

//    private void showCalorieData() {
//        int kontoID = SessionManager.getAktivtKontoID();
//        if (kontoID == -1) return;
//
//        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
//        LocalDate endDate = LocalDate.now();
//
//        CalorieLogDAO dao = new CalorieLogDAO();
//        List<CalorieLog> logs = dao.getLogsForDateRange(startDate, endDate, kontoID);
//
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("Kalorier");
//
//        for (CalorieLog logg : logs) {
//            int days = logg.getDatum().getDayOfMonth();
//            series.getData().add(new XYChart.Data<>(days, logg.getKalorier()));
//        }
//
//        calorieChart.getData().add(series);
//    }

    private void showCalorieData() {
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();

        CalorieLogDAO dao = new CalorieLogDAO();
        List<CalorieLog> logs = dao.getLogsForDateRange(startDate, endDate, kontoID);

        // Gruppera kalorier per dag (summera vid flera poster samma dag)
        Map<Integer, Integer> kalorierPerDag = new TreeMap<>();
        for (CalorieLog logg : logs) {
            int dag = logg.getDatum().getDayOfMonth();
            kalorierPerDag.put(dag, kalorierPerDag.getOrDefault(dag, 0) + logg.getKalorier());
        }

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Kalorier");

        for (Map.Entry<Integer, Integer> entry : kalorierPerDag.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        calorieChart.getData().add(series);
    }

}
