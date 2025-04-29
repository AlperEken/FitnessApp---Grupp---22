package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.FitnessApp1.model.Konto;
import org.FitnessApp1.model.KontoDAO;
import org.FitnessApp1.model.SessionManager;

public class EditProfileScreen {

    private VBox layout;
    private TextField nameField;
    private TextField efternameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField weightField;
    private TextField genderField;
    private TextField goalField;
    private Button saveButton;
    private Button deleteButton;

    private final Konto originalKonto;

    public EditProfileScreen(Konto konto) {
        this.originalKonto = konto;

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        nameField = new TextField(konto.getNamn());
        efternameField = new TextField(konto.getEfternamn());
        emailField = new TextField(konto.getEpost());
        passwordField = new PasswordField();
        weightField = new TextField(String.valueOf(konto.getVikt()));
        genderField = new TextField(konto.getKön());
        goalField = new TextField(String.valueOf(konto.getDagligtMal()));
        saveButton = new Button("Spara");
        deleteButton = new Button("Radera konto");

        layout.getChildren().addAll(
                new Label("Förnamn:"), nameField,
                new Label("Efternamn:"), efternameField,
                new Label("E-post:"), emailField,
                new Label("Lösenord (lämna tomt om du inte vill ändra):"), passwordField,
                new Label("Vikt (kg):"), weightField,
                new Label("Kön:"), genderField,
                new Label("Dagligt mål (kalorier):"), goalField,
                saveButton,
                deleteButton
        );

        saveButton.setOnAction(e -> sparaÄndringar());
        deleteButton.setOnAction(e -> raderaKonto());
    }

    private void sparaÄndringar() {
        try {
            String nyttNamn = nameField.getText();
            String nyttEfternamn = efternameField.getText();
            String nyttEpost = emailField.getText();
            String nyttLösen = passwordField.getText();
            double nyVikt = Double.parseDouble(weightField.getText());
            String nyttKön = genderField.getText();
            int nyttMål = Integer.parseInt(goalField.getText());

            String lösenAttSpara = nyttLösen.isEmpty() ? originalKonto.getLösenord() : nyttLösen;

            Konto uppdateratKonto = new Konto(
                    originalKonto.getKontoID(),
                    nyttNamn,
                    nyttEfternamn,
                    nyttEpost,
                    lösenAttSpara,
                    originalKonto.getÅlder(),
                    nyVikt,
                    nyttKön,
                    nyttMål
            );

            KontoDAO kontoDAO = new KontoDAO();
            boolean lyckades = kontoDAO.uppdateraKonto(uppdateratKonto);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Profiluppdatering");
            alert.setHeaderText(null);
            alert.setContentText(lyckades ? "Profilen har uppdaterats." : "Misslyckades att spara ändringar.");
            alert.showAndWait();

            if (lyckades) {
                ((Stage) layout.getScene().getWindow()).close();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fel vid uppdatering");
            alert.setContentText("Kontrollera att alla fält är korrekt ifyllda.");
            alert.showAndWait();
        }
    }

    private void raderaKonto() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Bekräftelse");
        confirm.setHeaderText("Är du säker?");
        confirm.setContentText("Detta kommer att permanent ta bort ditt konto.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                KontoDAO kontoDAO = new KontoDAO();
                boolean raderat = kontoDAO.raderaKonto(originalKonto.getKontoID());
                Alert resultat = new Alert(Alert.AlertType.INFORMATION);
                resultat.setHeaderText(null);
                resultat.setContentText(raderat ? "Konto har raderats." : "Kunde inte radera konto.");
                resultat.showAndWait();

                if (raderat) {
                    ((Stage) layout.getScene().getWindow()).close();
                    SessionManager.clearAktivtKontoID();
                    // Navigera tillbaka till startskärm om önskas
                }
            }
        });
    }

    public Parent getRoot() {
        return layout;
    }

    public void visaFönster() {
        Scene scene = new Scene(getRoot(), 400, 550);
        Stage stage = new Stage();
        stage.setTitle("Redigera Profil");
        stage.setScene(scene);
        stage.show();
    }
}
