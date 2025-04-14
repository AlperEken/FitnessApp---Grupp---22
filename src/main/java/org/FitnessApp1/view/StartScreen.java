package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Parent;

public class StartScreen {

    private VBox layout;
    private Button loginButton;
    private Button registerButton;

    public StartScreen() {
        buildUI();
    }

    private void buildUI() {
        layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-alignment: center;");

        Text welcomeText = new Text("Välkommen till FitnessApp!");
        welcomeText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        loginButton = new Button("Logga in");
        registerButton = new Button("Skapa konto");

        loginButton.setPrefWidth(200);
        registerButton.setPrefWidth(200);

        layout.getChildren().addAll(welcomeText, loginButton, registerButton);
    }

    public Parent getRoot() {
        return layout;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}