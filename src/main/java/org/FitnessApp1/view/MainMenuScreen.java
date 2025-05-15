package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button kalenderButton;

    public MainMenuScreen(String username) {
        this.username = username;

        layout = new VBox(25);
        layout.setPadding(new Insets(40));
        layout.setStyle("""
            -fx-alignment: center;
            -fx-background-color: linear-gradient(to bottom, #fdfdfd, #dbeafe);
        """);

        title = new Text("Välkommen, " + username + "!");
        title.setStyle("""
            -fx-font-size: 26px;
            -fx-font-weight: bold;
            -fx-text-fill: #1E3A8A;
        """);

        findGymsButton = new Button("🗺 Hitta utegym i Malmö");
        kalenderButton = new Button("Kalender");
        calorieLogButton = new Button("🍔 Logga kalorier");
        statisticsButton = new Button("📊 Visa statistik");
        editProfileButton = new Button("✏️ Redigera konto");
        loggaUtButton = new Button("🚪 Logga ut");
        findGymsButton.setPrefWidth(250);
        calorieLogButton.setPrefWidth(250);
        statisticsButton.setPrefWidth(250);
        loggaUtButton.setPrefWidth(250);
        editProfileButton.setPrefWidth(250);
        kalenderButton.setPrefWidth(250);

        styleButton(findGymsButton);
        styleButton(calorieLogButton);
        styleButton(statisticsButton);
        styleButton(editProfileButton);
        styleButton(loggaUtButton);
        styleButton(kalenderButton);

        layout.getChildren().addAll(
                title,
                findGymsButton,
                kalenderButton,
                calorieLogButton,
                statisticsButton,
                editProfileButton,
                loggaUtButton

        );
    }

    private void styleButton(Button button) {
        button.setStyle("""
        -fx-background-color: #1E3A8A;
        -fx-text-fill: white;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-background-radius: 30;
        -fx-padding: 10 30 10 30;
        -fx-cursor: hand;
    """);

        // Hover-effekt
        button.setOnMouseEntered(e -> button.setStyle("""
        -fx-background-color: #3B82F6;
        -fx-text-fill: white;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-background-radius: 30;
        -fx-padding: 10 30 10 30;
        -fx-cursor: hand;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 2);
    """));

        button.setOnMouseExited(e -> button.setStyle("""
        -fx-background-color: #1E3A8A;
        -fx-text-fill: white;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-background-radius: 30;
        -fx-padding: 10 30 10 30;
        -fx-cursor: hand;
    """));
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

    public Button getKalenderButton()
    {
        return kalenderButton;
    }
}
