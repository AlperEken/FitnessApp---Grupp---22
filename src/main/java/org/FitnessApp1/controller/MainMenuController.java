package org.FitnessApp1.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import org.FitnessApp1.model.Account;
import org.FitnessApp1.model.AccountDAO;
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
        view.getFindGymsButton().setOnAction(e -> mapsController.showGym(primaryStage));

        view.getStatisticsButton().setOnAction(e -> {
            StatisticScreen statisticScreen = new StatisticScreen();
            statisticScreen.showInPrimaryWindow();
        });

        view.getCalorieLogButton().setOnAction(e -> {
            CalorieLogScreen screen = new CalorieLogScreen();
            Scene scene = new Scene(screen.getRoot(), 600, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Logga kalorier");
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
                    Main.visaStartScreen(primaryStage);
                }
            });
        });

        view.getEditProfileButton().setOnAction(e -> {
            int kontoID = SessionManager.getAktivtKontoID();
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.getAccountByID(kontoID);

            if (account != null) {
                EditProfileScreen editProfileScreen = new EditProfileScreen(account, primaryStage);
                Scene scene = new Scene(editProfileScreen.getRoot(), 400, 550);
                primaryStage.setScene(scene);
            } else {
                System.out.println("Konto hittades inte.");
            }
        });

        view.getKalenderButton().setOnAction(e -> {
           int kontoID = SessionManager.getAktivtKontoID();

            CalenderScreen calenderScreen = new CalenderScreen(kontoID);
            Scene scene = new Scene(calenderScreen.getCalendarView(), 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kalender");
        });


    }









}
