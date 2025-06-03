package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.FitnessApp1.Main;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.CalorieLog;
import org.FitnessApp1.model.CalorieLogDAO;
import org.FitnessApp1.model.SessionManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticScreen {

    private final BorderPane root;
    private final VBox layout;
    private final LineChart<Number, Number> calorieChart;
    private final ComboBox<YearMonth> monthSelector;
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("sv"));

    public StatisticScreen() {
        // === Root och layout ===
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(white, #e6f0ff);");

        layout = new VBox(20);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.TOP_CENTER);

        // === Header ===
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> {
            MainMenuScreen menuScreen = new MainMenuScreen(SessionManager.getUsername());
            new MainMenuController(menuScreen, Main.getPrimaryStage());
            Main.getPrimaryStage().setScene(new Scene(menuScreen.getRoot(), 800, 600));
        });

        VBox titleBox = new VBox(3);
        Label title = new Label("Statistik");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #1A3E8B;");
        Label subtitle = new Label("Se din utveckling av kaloriintag över tid.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: #555;");
        titleBox.getChildren().addAll(title, subtitle);

        header.getChildren().addAll(homeIcon, titleBox);

        // === Månadsväljare ===
        Label monthLabel = new Label("Välj månad:");
        monthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        monthLabel.setStyle("-fx-text-fill: #333;");
        monthSelector = new ComboBox<>();
        monthSelector.setStyle("""
    -fx-background-color: #1A3E8B;
    -fx-background-radius: 20px;
    -fx-padding: 6 16 6 16;
""");

        monthSelector.setButtonCell(new ComboBoxCell(true)); // vit text i "knappen"
        monthSelector.setCellFactory(param -> new ComboBoxCell(false)); // svart text i listan



        // Fyll senaste 12 månader
        YearMonth currentMonth = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            YearMonth ym = currentMonth.minusMonths(i);
            monthSelector.getItems().add(ym);
        }

        monthSelector.setValue(currentMonth);
        monthSelector.setOnAction(e -> showCalorieData());

        VBox monthBox = new VBox(5, monthLabel, monthSelector);
        monthBox.setAlignment(Pos.CENTER_LEFT);

        // === Axlar och diagram ===
        NumberAxis xAxis = new NumberAxis(1, currentMonth.lengthOfMonth(), 1);
        xAxis.setLabel("Dagar");
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%.0f", object.doubleValue());
            }
        });

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Kalorier");

        calorieChart = new LineChart<>(xAxis, yAxis);
        calorieChart.setTitle("Kalorier per dag");
        calorieChart.setCreateSymbols(false);
        calorieChart.setLegendVisible(false);
        calorieChart.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12px;
            -fx-border-radius: 12px;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);
        """);
        calorieChart.setMinHeight(400);

        showCalorieData();

        layout.getChildren().addAll(header, monthBox, calorieChart);
        root.setCenter(layout);
    }

    public Parent getRoot() {
        return root;
    }

    public void showInPrimaryWindow() {
        Scene statScene = new Scene(getRoot(), 800, 600);
        Main.getPrimaryStage().setScene(statScene);
    }

    private void showCalorieData() {
        YearMonth selectedMonth = monthSelector.getValue();
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.atEndOfMonth();

        List<CalorieLog> logs = new CalorieLogDAO().getLogsForDateRange(startDate, endDate, kontoID);

        Map<Integer, Integer> kalorierPerDag = new TreeMap<>();
        for (CalorieLog logg : logs) {
            int dag = logg.getDatum().getDayOfMonth();
            kalorierPerDag.put(dag, kalorierPerDag.getOrDefault(dag, 0) + logg.getKalorier());
        }

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 1; i <= selectedMonth.lengthOfMonth(); i++) {
            series.getData().add(new XYChart.Data<>(i, kalorierPerDag.getOrDefault(i, 0)));
        }

        calorieChart.getData().clear();
        calorieChart.getXAxis().setAutoRanging(false);
        ((NumberAxis) calorieChart.getXAxis()).setUpperBound(selectedMonth.lengthOfMonth());
        calorieChart.getData().add(series);
    }

<<<<<<< Updated upstream
=======
    // === Formatterad cellklass för ComboBox ===
>>>>>>> Stashed changes
    private static class ComboBoxCell extends javafx.scene.control.ListCell<YearMonth> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("sv"));
        private final boolean isButton;

        public ComboBoxCell(boolean isButton) {
            this.isButton = isButton;
        }

        @Override
        protected void updateItem(YearMonth item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.format(formatter));
                if (isButton) {
                    setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: black; -fx-font-weight: normal;");
                }
            }
        }
    }


}
