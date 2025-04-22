package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginScreen {

    private VBox layout;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginScreen() {
        buildUI();
    }


    private void buildUI() {
        layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-alignment: center;");

        Text title = new Text("Logga in");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        emailField = new TextField();
        emailField.setPromptText("E-post");

        passwordField = new PasswordField();
        passwordField.setPromptText("Lösenord");

        loginButton = new Button("Logga in");
        loginButton.setPrefWidth(200);

        layout.getChildren().addAll(title, emailField, passwordField, loginButton);
    }

    public Parent getRoot() {
        return layout;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}