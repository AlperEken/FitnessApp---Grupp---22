package org.FitnessApp1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.FitnessApp1.view.StartScreen;
import org.FitnessApp1.view.LoginScreen;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.view.MainMenuScreen;
import org.FitnessApp1.model.KontoDAO;
import org.FitnessApp1.model.Konto;
import org.FitnessApp1.model.SessionManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.FitnessApp1.view.RegisterScreen;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartScreen startScreen = new StartScreen();
        Scene scene = new Scene(startScreen.getRoot(), 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FitnessApp1");
        primaryStage.show();

        startScreen.getLoginButton().setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();

            loginScreen.getLoginButton().setOnAction(loginEvent -> {
                String email = loginScreen.getEmailField().getText();
                String password = loginScreen.getPasswordField().getText();

                KontoDAO kontoDAO = new KontoDAO();
                boolean isLoggedIn = kontoDAO.valideraInloggning(email, password);

                if (isLoggedIn) {
                    String namn = kontoDAO.getNameByEmail(email);

                    if (namn != null) {
                        int kontoID = kontoDAO.getAcoountIDByEmail(email);
                        SessionManager.setAktivtKontoID(kontoID);
                        SessionManager.setUsername(namn); // ✅ Lägg till namn i session

                        MainMenuScreen mainMenuScreen = new MainMenuScreen(namn);
                        new MainMenuController(mainMenuScreen, primaryStage);

                        Scene mainMenuScene = new Scene(mainMenuScreen.getRoot(), 800, 600);
                        primaryStage.setScene(mainMenuScene);
                    } else {
                        System.out.println("Kunde inte hämta namn från databasen.");
                    }
                } else {
                    System.out.println("Felaktig e-post eller lösenord");
                }
            });

            Scene loginScene = new Scene(loginScreen.getRoot(), 400, 300);
            primaryStage.setScene(loginScene);
        });

        startScreen.getRegisterButton().setOnAction(e -> {
            VisaRegistreringsskärm(primaryStage);
        });
    }

    private void VisaRegistreringsskärm(Stage primaryStage) {
        RegisterScreen registerScreen = new RegisterScreen();

        registerScreen.getRegisterButton().setOnAction(regEvent -> {
            String namn = registerScreen.getNameField().getText();
            String efternamn = registerScreen.getLastnameField().getText();
            String epost = registerScreen.getEmailField().getText();
            String password = registerScreen.getPasswordField().getText();
            double vikt = Double.parseDouble(registerScreen.getWeightField().getText());
            String kön = registerScreen.getGenderField().getText();
            int dagligtMal = Integer.parseInt(registerScreen.getGoalField().getText());

            KontoDAO kontoDAO = new KontoDAO();
            Konto konto = new Konto(namn, efternamn, epost, password, 0, vikt, kön, dagligtMal);
            boolean registrerad = kontoDAO.registeraccount(konto);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registrering");
            alert.setHeaderText(null);
            alert.setContentText(registrerad ? "Kontot har skapats!" : "Kunde inte skapa konto.");
            alert.showAndWait();
        });

        Scene registreringScene = new Scene(registerScreen.getRoot(), 400, 400);
        primaryStage.setScene(registreringScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
