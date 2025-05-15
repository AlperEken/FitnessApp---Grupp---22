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
import org.FitnessApp1.Main;
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

        // Skapa knapp för att stänga fönstret
        backButton = new Button("Stäng");
        backButton.setOnAction(e -> ((Stage) backButton.getScene().getWindow()).close());

        layout.getChildren().addAll(calorieChart, backButton);
    }

    public Parent getRoot() {
        return layout;
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
