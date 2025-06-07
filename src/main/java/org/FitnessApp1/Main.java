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
        primaryStageRef = primaryStage;
        showStartScreen(primaryStage);
    }

    public static void showStartScreen(Stage stage) {
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
                    AdminDAO adminDAO = new AdminDAO();
                    boolean isAdminLoggedIn = adminDAO.validateAdmin(email, password);

                    if (isAdminLoggedIn) {
                        AdminDashboard adminDashboard = new AdminDashboard(stage, loginScreen);
                        adminDashboard.view();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Admin-inloggning misslyckades");
                        alert.setHeaderText(null);
                        alert.setContentText("Felaktig admin-e-post eller lösenord.");
                        alert.showAndWait();
                    }
                } else {
                    AccountDAO accountDAO = new AccountDAO();
                    boolean isLoggedIn = accountDAO.validateLogIn(email, password);

                    if (isLoggedIn) {
                        String namn = accountDAO.getNameByEmail(email);
                        if (namn != null) {
                            int kontoID = accountDAO.getAcoountIDByEmail(email);
                            SessionManager.setActiveAccountID(kontoID);
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
            showRegistrationScreen(stage);
        });
    }

    public static void showRegistrationScreen(Stage stage) {
        RegisterScreen registerScreen = new RegisterScreen(primaryStageRef);

        registerScreen.getRegisterButton().setOnAction(regEvent -> {
            try {
                String name = registerScreen.getNameField().getText();
                String lastName = registerScreen.getLastnameField().getText();
                String email = registerScreen.getEmailField().getText();
                String password = registerScreen.getPasswordField().getText();
                double weight = Double.parseDouble(registerScreen.getWeightField().getText());
                String gender = registerScreen.getGenderField().getText();
                int dailyGoal = Integer.parseInt(registerScreen.getGoalField().getText());

                Account account = new Account(name, lastName, email, password, 0, weight, gender, dailyGoal);
                AccountDAO accountDAO = new AccountDAO();
                boolean registered = accountDAO.registerAccount(account);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registrering");
                alert.setHeaderText(null);
                alert.setContentText(registered ? "Kontot har skapats!" : "Kunde inte skapa konto.");
                alert.showAndWait();

                if (registered) {
                    showStartScreen(stage);
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fel");
                alert.setContentText("Fyll i alla fält korrekt.");
                alert.showAndWait();
            }
        });

        Scene registreringScene = new Scene(registerScreen.getRoot(), 600, 650);
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
