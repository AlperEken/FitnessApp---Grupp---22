package org.FitnessApp1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.AdminDAO;
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


    public static void visaStartScreen(Stage stage) {
        StartScreen startScreen = new StartScreen(stage);
        Scene scene = new Scene(startScreen.getRoot(), 500, 400);
        stage.setScene(scene);
        stage.setTitle("FitnessApp");
        stage.show();

        startScreen.getLoginButton().setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            Scene loginScene = new Scene(loginScreen.getRoot(), 600, 550);
            stage.setScene(loginScene);
            stage.centerOnScreen();

            loginScreen.getLoginButton().setOnAction(loginEvent -> {
                String email = loginScreen.getEmailField().getText();
                String password = loginScreen.getPasswordField().getText();

                if (loginScreen.getAdminToggle().isSelected()) {
                    // === Admininloggning ===
                    AdminDAO adminDAO = new AdminDAO();
                    boolean isAdminLoggedIn = adminDAO.validateAdmin(email, password);

                    if (isAdminLoggedIn) {
                        AdminDashboard adminDashboard = new AdminDashboard(stage);
                        adminDashboard.view();  // Du skapar denna metod
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Admin-inloggning misslyckades");
                        alert.setHeaderText(null);
                        alert.setContentText("Felaktig admin-e-post eller lösenord.");
                        alert.showAndWait();
                    }
                } else {
                    // === Vanlig användarinloggning ===
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
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Inloggning misslyckades");
                        alert.setHeaderText(null);
                        alert.setContentText("Felaktig e-post eller lösenord. Försök igen.");
                        alert.showAndWait();
                    }
                }
            });
        });

        startScreen.getRegisterButton().setOnAction(e -> {
            visaRegistreringsskärm(stage);
        });
    }


    public static void visaRegistreringsskärm(Stage stage) {
        RegisterScreen registerScreen = new RegisterScreen(primaryStageRef);

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

                if (registrerad) {
                    visaStartScreen(stage);
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fel");
                alert.setContentText("Fyll i alla fält korrekt.");
                alert.showAndWait();
            }
        });

        Scene registreringScene = new Scene(registerScreen.getRoot(), 600, 600);
        stage.setScene(registreringScene);
        stage.centerOnScreen();

    }

    public static Stage getPrimaryStage() {
        return primaryStageRef;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
