package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import org.FitnessApp1.model.Account;
import org.FitnessApp1.model.AccountDAO;

public class RegisterScreen {


    private VBox layout;
    private ScrollPane scrollPane;
    private TextField nameField;
    private TextField lastnameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField ageField;
    private TextField weightField;
    private TextField genderField;
    private TextField goalField;
    private Button registerButton;
    private Stage stage;

    public RegisterScreen(Stage primaryStageRef) {
        this.stage = primaryStageRef;

        BorderPane root = new BorderPane();
        root.setStyle("""
    -fx-background-color: linear-gradient(
        from 0% 0% to 100% 100%,
        #26c6da,  /* turkos */
        #00838f,  /* petrolblå */
        #283593   /* djupblå */
    );
""");

        VBox layoutCard = new VBox(5);
        layoutCard.setPadding(new Insets(10));
        layoutCard.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        layoutCard.setMaxWidth(400);
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
        Text title = new Text("Skapa konto");
        title.setStyle("-fx-font-family: 'italic';");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1A3E8B;");
        headerBox.getChildren().addAll(title);

        // === Fält ===
        nameField = new TextField(); nameField.setPromptText("Förnamn");
        lastnameField = new TextField(); lastnameField.setPromptText("Efternamn");
        emailField = new TextField(); emailField.setPromptText("E-post");
        passwordField = new PasswordField(); passwordField.setPromptText("Lösenord");
        ageField = new TextField(); ageField.setPromptText("Ålder");
        weightField = new TextField(); weightField.setPromptText("Vikt (kg)");
        genderField = new TextField(); genderField.setPromptText("Kön");
        goalField = new TextField(); goalField.setPromptText("Dagligt mål (kalorier)");

        // === Knapp ===
        registerButton = new Button("Skapa konto");
        registerButton.setPrefWidth(250);
        registerButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: #0F2A5C; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;"));

        // === Lägg till i kortet ===
        layoutCard.getChildren().addAll(
                headerBox,

                createLabel("Förnamn:"), nameField,
                createLabel("Efternamn:"), lastnameField,
                createLabel("E-post:"), emailField,
                createLabel("Lösenord:"), passwordField,
                createLabel("Ålder:"), ageField,
                createLabel("Vikt (kg):"), weightField,
                createLabel("Kön:"), genderField,
                createLabel("Dagligt mål (kalorier):"), goalField,

                registerButton
        );



        VBox wrapper = new VBox(layoutCard);
        wrapper.setPadding(new Insets(50));
        wrapper.setStyle("-fx-alignment: center;");

        root.setTop(homeIcon);
        BorderPane.setMargin(homeIcon, new Insets(20, 0, 0, 20));
        root.setCenter(wrapper);

        layout = new VBox(root); // så getRoot() fungerar

        // === Registreringslogik ===
        registerButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String lastname = lastnameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                int age = Integer.parseInt(ageField.getText());
                double weight = Double.parseDouble(weightField.getText());
                String gender = genderField.getText();
                int goal = Integer.parseInt(goalField.getText());

                Account account = new Account(name, lastname, email, password, age, weight, gender, goal);
                AccountDAO accountDAO = new AccountDAO();
                boolean isCreated = accountDAO.registerAccount(account);

                Alert alert = new Alert(isCreated ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle("Registrering");
                alert.setContentText(isCreated ? "Ditt konto har skapats!" : "Kunde inte skapa kontot.");
                alert.showAndWait();

                if (isCreated) {
                    Main.showStartScreen(Main.getPrimaryStage());
                }




            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fel");
                alert.setContentText("Fyll i alla fält korrekt.");
                alert.showAndWait();
            }
        });
    }


    public Parent getRoot() {
        return layout;
    }

    // Getters
    public TextField getNameField() { return nameField; }
    public TextField getLastnameField() { return lastnameField; }
    public TextField getEmailField() { return emailField; }
    public PasswordField getPasswordField() { return passwordField; }
    public TextField getWeightField() { return weightField; }
    public TextField getGenderField() { return genderField; }
    public TextField getGoalField() { return goalField; }
    public Button getRegisterButton() { return registerButton; }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: italic; -fx-font-weight: bold; -fx-text-fill: black;");
        return label;
    }

}
