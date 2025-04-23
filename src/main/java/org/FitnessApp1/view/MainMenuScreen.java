package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainMenuScreen {

    private VBox layout;
    private Button findGymsButton;
    private Button calorieLogButton;
    private Button statisticsButton;
    private Button loggaUtButton;
    private Button editProfileButton; //  För redigering av konto
    private Button calendarButton;

    public MainMenuScreen(String username) {
        layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        Text title = new Text("Välkommen, " + username + " till FitnessApp!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        findGymsButton = new Button("🗺️ Hitta utegym i Malmö");
        calorieLogButton = new Button("🍔 Logga kalorier");
        statisticsButton = new Button("📊 Visa statistik");
        loggaUtButton = new Button("🚪 Logga ut");
        editProfileButton = new Button("✏️ Redigera konto"); // 🆕
        calendarButton = new Button("Show your calendar"); // Knapp för kalender

        findGymsButton.setPrefWidth(250);
        calorieLogButton.setPrefWidth(250);
        statisticsButton.setPrefWidth(250);
        loggaUtButton.setPrefWidth(250);
        editProfileButton.setPrefWidth(250);
        calendarButton.setPrefWidth(250);

        layout.getChildren().addAll(
                title,
                findGymsButton,
                calorieLogButton,
                statisticsButton,
                loggaUtButton,
                editProfileButton,
                calendarButton
        );
    }

    public Parent getRoot() {
        return layout;
    }

    public Button getFindGymsButton() {
        return findGymsButton;
    }

    public Button getCalorieLogButton() {
        return calorieLogButton;
    }

    public Button getStatisticsButton() {
        return statisticsButton;
    }

    public Button getLoggaUtButton() {
        return loggaUtButton;
    }

    public Button getEditProfileButton() {
        return editProfileButton;
    }

    public Button getCalendarButton()
    {
        return calendarButton;
    }

}
