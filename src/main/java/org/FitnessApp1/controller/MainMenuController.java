package org.FitnessApp1.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.FitnessApp1.model.KontoDAO;
import org.FitnessApp1.view.KalenderScreen;
import org.FitnessApp1.view.MainMenuScreen;
import org.FitnessApp1.view.EditProfileScreen;
import org.FitnessApp1.model.Konto;
import org.FitnessApp1.model.SessionManager;
import org.FitnessApp1.view.KaloriLoggScreen;

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

        // 🍔 Logga kalorier
        view.getCalorieLogButton().setOnAction(e -> {
            KaloriLoggScreen kaloriLoggScreen = new KaloriLoggScreen();
            Scene kaloriScene = new Scene(kaloriLoggScreen.getRoot(), 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Logga kalorier");
            stage.setScene(kaloriScene);
            stage.show();
        });

        // 📊 Visa statistik
        view.getStatisticsButton().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistik");
            alert.setHeaderText("Funktion kommer snart");
            alert.setContentText("Här kommer du se grafer och statistik över din aktivitet.");
            alert.showAndWait();
        });

        // 🚪 Logga ut
        view.getLoggaUtButton().setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logga ut");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Vill du logga ut?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    SessionManager.clearAktivtKontoID();
                    System.out.println("Användare har loggats ut.");
                }
            });
        });

        // ✏️ Redigera konto
        view.getEditProfileButton().setOnAction(e -> {
            int kontoID = SessionManager.getAktivtKontoID();
            KontoDAO kontoDAO = new KontoDAO();
            Konto konto = kontoDAO.hämtaKontoByID(kontoID);

            if (konto != null) {
                EditProfileScreen editProfileScreen = new EditProfileScreen(konto);
                editProfileScreen.visaFönster();
            } else {
                System.out.println("Konto hittades inte.");
            }
        });

        // 📅 Öppna kalender
        view.getCalendarButton().setOnAction(e -> {
            KalenderScreen kalenderScreen = new KalenderScreen();
            kalenderScreen.visaFönster();
        });
    }


}
