package org.FitnessApp1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.view.*;
import org.FitnessApp1.model.AccountDAO;
import org.FitnessApp1.model.Account;
import org.FitnessApp1.model.SessionManager;
import javafx.scene.control.Alert;

public class Main extends Application {

    private static Stage primaryStageRef;

    @Override
    public void start(Stage primaryStage) {
        primaryStageRef = primaryStage; // Spara för tillgång vid utloggning
        visaStartScreen(primaryStage);
    }

    // ✅ Visar StartScreen och kopplar knapparna
    public static void visaStartScreen(Stage stage) {
        StartScreen startScreen = new StartScreen();
        Scene scene = new Scene(startScreen.getRoot(), 500, 400);
        stage.setScene(scene);
        stage.setTitle("FitnessApp");
        stage.show();

        startScreen.getLoginButton().setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            Scene loginScene = new Scene(loginScreen.getRoot(), 400, 300);
            stage.setScene(loginScene);

            loginScreen.getLoginButton().setOnAction(loginEvent -> {
                String email = loginScreen.getEmailField().getText();
                String password = loginScreen.getPasswordField().getText();

                AccountDAO accountDAO = new AccountDAO();
                boolean isLoggedIn = accountDAO.valideraInloggning(email, password);

                if (isLoggedIn) {
                    String namn = accountDAO.getNameByEmail(email);
                    if (namn != null) {
                        int kontoID = accountDAO.getAcoountIDByEmail(email);
                        SessionManager.setAktivtKontoID(kontoID);
                        SessionManager.setUsername(namn);

                        MainMenuScreen mainMenuScreen = new MainMenuScreen(namn);
                        new MainMenuController(mainMenuScreen, stage);
                        Scene mainMenuScene = new Scene(mainMenuScreen.getRoot(), 800, 600);
                        stage.setScene(mainMenuScene);
                    }
                } else {
                    System.out.println("Felaktig e-post eller lösenord");
                }
            });
        });

        startScreen.getRegisterButton().setOnAction(e -> {
            visaRegistreringsskärm(stage);
        });
    }

    // ✅ Visar RegisterScreen
    public static void visaRegistreringsskärm(Stage stage) {
        RegisterScreen registerScreen = new RegisterScreen();

        registerScreen.getRegisterButton().setOnAction(regEvent -> {
            try {
                String namn = registerScreen.getNameField().getText();
                String efternamn = registerScreen.getLastnameField().getText();
                String epost = registerScreen.getEmailField().getText();
                String password = registerScreen.getPasswordField().getText();
                double vikt = Double.parseDouble(registerScreen.getWeightField().getText());
                String kön = registerScreen.getGenderField().getText();
                int dagligtMal = Integer.parseInt(registerScreen.getGoalField().getText());

                Account account = new Account(namn, efternamn, epost, password, 0, vikt, kön, dagligtMal);
                AccountDAO accountDAO = new AccountDAO();
                boolean registrerad = accountDAO.registeraccount(account);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registrering");
                alert.setHeaderText(null);
                alert.setContentText(registrerad ? "Kontot har skapats!" : "Kunde inte skapa konto.");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fel");
                alert.setContentText("Fyll i alla fält korrekt.");
                alert.showAndWait();
            }
        });

        Scene registreringScene = new Scene(registerScreen.getRoot(), 400, 400);
        stage.setScene(registreringScene);
    }

    public static Stage getPrimaryStage() {
        return primaryStageRef;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
