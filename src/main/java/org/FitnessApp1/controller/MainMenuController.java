package org.FitnessApp1.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import org.FitnessApp1.model.Konto;
import org.FitnessApp1.model.KontoDAO;
import org.FitnessApp1.model.SessionManager;
import org.FitnessApp1.view.*;

public class MainMenuController {

    private final MainMenuScreen view;
    private final Stage primaryStage;
    private final GoogleMapsController mapsController;

    public MainMenuController(MainMenuScreen view, Stage primaryStage) {
        this.view = view;
        this.primaryStage = primaryStage;
        this.mapsController = new GoogleMapsController();
        initHandlers();
    }

    private void initHandlers() {
        view.getFindGymsButton().setOnAction(e -> mapsController.showGym());

        view.getStatisticsButton().setOnAction(e -> {
            StatisticScreen statisticScreen = new StatisticScreen();
            statisticScreen.showInNewWindow();
        });

        view.getCalorieLogButton().setOnAction(e -> {
            KaloriLoggScreen kaloriLoggScreen = new KaloriLoggScreen();
            Scene kaloriScene = new Scene(kaloriLoggScreen.getRoot(), 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Logga kalorier");
            stage.setScene(kaloriScene);
            stage.show();
        });

        view.getLoggaUtButton().setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logga ut");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Vill du logga ut?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    SessionManager.clearAktivtKontoID();
                    System.out.println("Användare har loggats ut.");

                    // Anropa metod från Main för att visa StartScreen med rätt logik
                    Main.visaStartScreen(primaryStage);
                }
            });
        });

        view.getEditProfileButton().setOnAction(e -> {
            int kontoID = SessionManager.getAktivtKontoID();
            KontoDAO kontoDAO = new KontoDAO();
            Konto konto = kontoDAO.getAccountByID(kontoID);

            if (konto != null) {
                EditProfileScreen editProfileScreen = new EditProfileScreen(konto);
                editProfileScreen.visaFönster();
            } else {
                System.out.println("Konto hittades inte.");
            }
        });
    }
}
