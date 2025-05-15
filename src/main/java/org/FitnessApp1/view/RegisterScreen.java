package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

import javafx.stage.Stage;
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

    public RegisterScreen() {
        layout = new VBox(10);
        layout.setPadding(new Insets(30));

        nameField = new TextField();
        lastnameField = new TextField();
        emailField = new TextField();
        passwordField = new PasswordField();
        ageField = new TextField();
        weightField = new TextField();
        genderField = new TextField();
        goalField = new TextField();
        registerButton = new Button("Skapa konto");

        layout.getChildren().addAll(
                new Label("Förnamn:"), nameField,
                new Label("Efternamn:"), lastnameField,
                new Label("E-post:"), emailField,
                new Label("Lösenord:"), passwordField,
                new Label("Ålder:"), ageField,
                new Label("Vikt (kg):"), weightField,
                new Label("Kön:"), genderField,
                new Label("Dagligt mål (kalorier):"), goalField,
                registerButton
        );

        scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        // När användaren trycker på register knappen
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

                // Skapa konto objekt
                Account account = new Account(name, lastname, email, password, age, weight, gender, goal);

                // Spara kontot i databasen via DAO
                AccountDAO accountDAO = new AccountDAO();
                boolean isCreated = accountDAO.registeraccount(account);

                if (isCreated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registrering");
                    alert.setContentText("Ditt konto har skapats!");
                    alert.showAndWait();

                    // Stäng detta fönster
                    closeRegisterWindow();

                    // Öppna login screen
                    openLoginScreen();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fel");
                    alert.setContentText("Kunde inte skapa kontot. Försök igen.");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fel");
                alert.setContentText("Fyll i alla fält korrekt.");
                alert.showAndWait();
            }
        });
    }

    // Öppna login screen
    private void openLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(); // Se till att skapa LoginScreen om den inte finns
        Stage loginStage = new Stage();
        loginStage.setTitle("Logga in");
        loginStage.setScene(new Scene(loginScreen.getRoot(), 300, 200)); // Justera storlek
        loginStage.show();
    }

    // Stäng registreringsfönstret
    private void closeRegisterWindow() {
        if (stage != null) {
            stage.close();
        }
    }

    public Parent getRoot() {
        return scrollPane;
    }

    // Metod för att sätta stage för fönstret (för att stänga det senare)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Getters
    public TextField getNameField() { return nameField; }
    public TextField getLastnameField() { return lastnameField; }
    public TextField getEmailField() { return emailField; }
    public PasswordField getPasswordField() { return passwordField; }
    public TextField getAgeField() { return ageField; }
    public TextField getWeightField() { return weightField; }
    public TextField getGenderField() { return genderField; }
    public TextField getGoalField() { return goalField; }
    public Button getRegisterButton() { return registerButton; }
}
