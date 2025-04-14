package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegisterScreen {

    private VBox layout;
    private TextField nameField;
    private TextField lastnameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField ageField;
    private TextField weightField;
    private TextField genderField;
    private TextField goalField;
    private Button registerButton;

    public RegisterScreen() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));

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
    }

    public Parent getRoot() {
        return layout;
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