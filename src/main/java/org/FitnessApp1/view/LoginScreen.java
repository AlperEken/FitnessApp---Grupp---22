package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class LoginScreen {

    private final Stage stage;
    private Parent layout;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginScreen(Stage stage) {
        this.stage = stage;
        buildUI();
    }


    private void buildUI() {
//        layout = new VBox(15);
//        layout.setPadding(new Insets(30));
//        layout.setStyle("-fx-alignment: center;");

        BorderPane layoutRoot = new BorderPane(); // Ny layouttyp
        layoutRoot.setPadding(new Insets(20));

        VBox centerBox = new VBox(15);
        centerBox.setStyle("-fx-alignment: center;");
        Image homeImage = new Image(getClass().getResourceAsStream("/images/home.png")); // byt till rätt filnamn!
        ImageView homeIcon = new ImageView(homeImage);
        homeIcon.setFitWidth(24);
        homeIcon.setFitHeight(24);
        homeIcon.setPreserveRatio(true);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));

        layoutRoot.setTop(homeIcon);        // ikonen i toppen
        layoutRoot.setCenter(centerBox);    // formulär i mitten

        layout = layoutRoot; // Sätt som klassens "layout" för getRoot()


        Text title = new Text("Logga in");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        emailField = new TextField();
        emailField.setPromptText("E-post");

        passwordField = new PasswordField();
        passwordField.setPromptText("Lösenord");

        loginButton = new Button("Logga in");
        loginButton.setPrefWidth(200);



// Klickhändelse
        homeIcon.setOnMouseClicked(e -> {
            Main.visaStartScreen(stage);
        });




        centerBox.getChildren().addAll(title, emailField, passwordField, loginButton);
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