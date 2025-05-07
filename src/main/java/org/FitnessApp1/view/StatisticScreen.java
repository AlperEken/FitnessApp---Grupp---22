package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.FitnessApp1.model.CalorieLog;
import org.FitnessApp1.model.CalorieLogDAO;
import org.FitnessApp1.model.SessionManager;

import java.time.LocalDate;
import java.util.List;

public class StatisticScreen {

    private VBox layout;
    private Button backButton;
    private LineChart<Number, Number> calorieChart;

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

        // Skapa knapp för att stänga fönstret
        backButton = new Button("Stäng");
        backButton.setOnAction(e -> ((Stage) backButton.getScene().getWindow()).close());

        layout.getChildren().addAll(calorieChart, backButton);
    }

    public Parent getRoot() {
        return layout;
    }

    public void showInNewWindow() {
        Stage stage = new Stage();
        stage.setTitle("Statistik");
        Scene scene = new Scene(getRoot(), 800, 600);
        stage.setScene(scene);
        stage.show();
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
