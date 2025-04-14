package org.FitnessApp1.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.FitnessApp1.view.MainMenuScreen;
import org.FitnessApp1.view.EditProfileScreen;
import org.FitnessApp1.model.Konto;
import org.FitnessApp1.model.SessionManager;
import org.FitnessApp1.view.KaloriLoggScreen;
import org.FitnessApp1.model.KontoDAO;

public class MainMenuController {

    private final MainMenuScreen view;
    private final GoogleMapsController mapsController;

    public MainMenuController(MainMenuScreen view) {
        this.view = view;
        this.mapsController = new GoogleMapsController();
        initHandlers();
    }

    private void initHandlers() {
        // 🗺️ Visa karta med utegym
        view.getFindGymsButton().setOnAction(e -> {
            mapsController.showGym();
        });

        // 🍔 Logga kalorier (placeholder)
        // 🍔 Logga kalorier
        view.getCalorieLogButton().setOnAction(e -> {
            KaloriLoggScreen kaloriLoggScreen = new KaloriLoggScreen();
            Scene kaloriScene = new Scene(kaloriLoggScreen.getRoot(), 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Logga kalorier");
            stage.setScene(kaloriScene);
            stage.show();
        });


        // 📊 Visa statistik (placeholder)
        view.getStatisticsButton().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistik");
            alert.setHeaderText("Funktion kommer snart");
            alert.setContentText("Här kommer du se grafer och statistik över din aktivitet.");
            alert.showAndWait();
        });

        // 🚪 Logga ut
        view.getLoggaUtButton().setOnAction(e -> {
            // Bekräftelse för att logga ut
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logga ut");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Vill du logga ut?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    SessionManager.clearAktivtKontoID(); // Loggar ut användaren
                    // Här kan du lägga till logik för att visa startskärmen igen eller inloggningsskärmen
                    System.out.println("Användare har loggats ut.");
                }
            });
        });

        // ✏️ Redigera konto
        view.getEditProfileButton().setOnAction(e -> {
            // Hämta konto-ID från SessionManager
            int kontoID = SessionManager.getAktivtKontoID();

            // Hämta konto från KontoDAO
            KontoDAO kontoDAO = new KontoDAO();
            Konto konto = kontoDAO.getAccountByID(kontoID);

            if (konto != null) {

                // Skapa EditProfileScreen och skicka konto-objektet till konstruktorn
                EditProfileScreen editProfileScreen = new EditProfileScreen(konto);

                // Visa EditProfileScreen-fönstret
                editProfileScreen.visaFönster();
            } else {
                // Hantera fallet där konto inte hittades
                System.out.println("Konto hittades inte.");
            }
        });
    }
}
