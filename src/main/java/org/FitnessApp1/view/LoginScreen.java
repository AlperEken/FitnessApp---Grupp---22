package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private ToggleButton adminToggle;


    public LoginScreen(Stage stage) {
        this.stage = stage;
        buildUI();
    }


    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setStyle("""
    -fx-background-color: linear-gradient(
        from 0% 0% to 100% 100%,
        #26c6da,  /* turkos */
        #00838f,  /* petrolblå */
        #283593   /* djupblå */
    );
""");


        VBox layoutCard = new VBox(15);
        layoutCard.setPadding(new Insets(30));
        layoutCard.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        layoutCard.setMaxWidth(350);
        layoutCard.setMinWidth(300);
        layoutCard.setStyle(layoutCard.getStyle() + "-fx-alignment: center;");

        // === Ikon + rubrik ===
        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> Main.showStartScreen(stage));
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));

        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(20, 0, 10, 0));
        Text title = new Text("Logga in");
        title.setStyle("-fx-font-family: 'italic';");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1A3E8B;");
        headerBox.getChildren().addAll(title);

        // === Inmatningsfält ===
        emailField = new TextField();
        emailField.setPromptText("E-post");
        emailField.setMaxWidth(250);

        passwordField = new PasswordField();
        passwordField.setPromptText("Lösenord");
        passwordField.setMaxWidth(250);

        // === Logga in-knapp ===
        loginButton = new Button("Logga in");
        loginButton.setPrefWidth(250);
        loginButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #0F2A5C; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;"));

        // === Toggle för adminläge ===
        adminToggle = new ToggleButton("Adminläge");
        adminToggle.setStyle("-fx-background-radius: 6;");
        adminToggle.setPrefWidth(250);

        // Lägg till i layouten
        layoutCard.getChildren().add(adminToggle);

        layoutCard.getChildren().addAll(headerBox, emailField, passwordField, loginButton);

        VBox wrapper = new VBox(layoutCard);
        wrapper.setPadding(new Insets(50));
        wrapper.setStyle("-fx-alignment: center;");

        root.setTop(homeIcon);
        BorderPane.setMargin(homeIcon, new Insets(20, 0, 0, 20));
        root.setCenter(wrapper);

        layout = root;
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

    public ToggleButton getAdminToggle() {
        return adminToggle;
    }

}