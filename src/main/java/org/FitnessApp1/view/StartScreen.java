package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class StartScreen {

    private Button loginButton;
    private Button registerButton;
    private StackPane root;
    private Stage stage;

    public StartScreen(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        // === Bakgrundsbild ===
        Image backgroundImage = new Image(getClass().getResourceAsStream("/view/Startsida.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setFitWidth(500);
        backgroundImageView.setFitHeight(450);

        // === Knappar ===
        loginButton = new Button("Log In");
        registerButton = new Button("Sign Up");

        styleRoundedButton(loginButton);
        styleRoundedButton(registerButton);

        loginButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            Scene loginScene = new Scene(loginScreen.getRoot(), 300, 200);
            stage.setScene(loginScene);
        });
//
//        registerButton.setOnAction(e -> {
//            RegisterScreen registerScreen = new RegisterScreen(stage);
//            Scene registerScene = new Scene(registerScreen.getRoot(), 400, 500);
//            stage.setScene(registerScene);
//        });


        HBox buttonBox = new HBox(20, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0, 0, 40, 0));

        VBox contentBox = new VBox(buttonBox);
        contentBox.setAlignment(Pos.BOTTOM_CENTER);

        // === Lägg till i root ===
        root = new StackPane(backgroundImageView, contentBox);
        StackPane.setAlignment(contentBox, Pos.BOTTOM_CENTER);
    }

    private void styleRoundedButton(Button button) {
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
        return root;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}
