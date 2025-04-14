package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.FitnessApp1.model.Konto;
import javafx.stage.Stage;

public class EditProfileScreen {

    private VBox layout;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField weightField;
    private TextField genderField;
    private TextField goalField;
    private Button saveButton;

    // Konstruktor som tar ett Konto-objekt för att fylla fälten
    public EditProfileScreen(Konto konto) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        nameField = new TextField(konto.getNamn());
        emailField = new TextField(konto.getEpost());
        passwordField = new PasswordField();
        weightField = new TextField(String.valueOf(konto.getVikt()));
        genderField = new TextField(konto.getKön());
        goalField = new TextField(String.valueOf(konto.getDagligtMal()));
        saveButton = new Button("Spara");

        layout.getChildren().addAll(
                new Label("Förnamn:"), nameField,
                new Label("E-post:"), emailField,
                new Label("Lösenord:"), passwordField,
                new Label("Vikt (kg):"), weightField,
                new Label("Kön:"), genderField,
                new Label("Dagligt mål (kalorier):"), goalField,
                saveButton
        );
    }

    public Parent getRoot() {
        return layout;
    }

    // Getter-metoder
    public TextField getNameField() {
        return nameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public TextField getWeightField() {
        return weightField;
    }

    public TextField getGenderField() {
        return genderField;
    }

    public TextField getGoalField() {
        return goalField;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    // Metod för att visa EditProfileScreen
    public void visaFönster() {
        Scene scene = new Scene(getRoot(), 400, 500);
        Stage stage = new Stage();
        stage.setTitle("Redigera Profil");
        stage.setScene(scene);
        stage.show();
    }
}