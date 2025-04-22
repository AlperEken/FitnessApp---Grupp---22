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
    private Button editProfileButton;
    private Text title;
    private String username; // Sparar användarnamnet

    public MainMenuScreen(String username) {
        this.username = username;

        layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        title = new Text("Välkommen, " + username + " till FitnessApp!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        findGymsButton = new Button("🗺 Hitta utegym i Malmö");
        calorieLogButton = new Button("🍔 Logga kalorier");
        statisticsButton = new Button("📊 Visa statistik");
        loggaUtButton = new Button("🚪 Logga ut");
        editProfileButton = new Button("✏️ Redigera konto");

        findGymsButton.setPrefWidth(250);
        calorieLogButton.setPrefWidth(250);
        statisticsButton.setPrefWidth(250);
        loggaUtButton.setPrefWidth(250);
        editProfileButton.setPrefWidth(250);

        layout.getChildren().addAll(
                title,
                findGymsButton,
                calorieLogButton,
                statisticsButton,
                loggaUtButton,
                editProfileButton
        );
    }

    public Parent getRoot() {
        return layout;
    }

    // Extra: metod för att uppdatera texten med användarnamn
    public void setUsername(String username) {
        this.username = username;
        title.setText("Välkommen, " + username + " till FitnessApp!");
    }

    public String getUsername() {
        return username;
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
}
