package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.Account;
import org.FitnessApp1.model.AccountDAO;
import org.FitnessApp1.model.SessionManager;

public class EditProfileScreen {

    private final Stage primaryStage;
    private final VBox layout = new VBox(15);
    private final TextField nameField, efternameField, emailField, weightField, genderField, goalField;
    private final PasswordField passwordField;
    private final Button saveButton, deleteButton;
    private final Account originalAccount;
    private final BorderPane root;

    public EditProfileScreen(Account account, Stage primaryStage) {
        this.originalAccount = account;
        this.primaryStage = primaryStage;

        // === Root och bakgrund ===
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(white, #e6f0ff);");

        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.setMaxWidth(400);
        layout.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // === Header ===
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> {
            MainMenuScreen menu = new MainMenuScreen(SessionManager.getUsername());
            new MainMenuController(menu, primaryStage);
            primaryStage.setScene(new Scene(menu.getRoot(), 800, 600));
        });

        VBox titleBox = new VBox(3);
        Label title = new Label("Redigera konto");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #1A3E8B;");
        Label subtitle = new Label("Uppdatera din profilinformation.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: #555;");
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(homeIcon, titleBox);

        // === Fält ===
        nameField = new TextField(account.getNamn());
        efternameField = new TextField(account.getEfternamn());
        emailField = new TextField(account.getEpost());
        passwordField = new PasswordField();
        weightField = new TextField(String.valueOf(account.getVikt()));
        genderField = new TextField(account.getKön());
        goalField = new TextField(String.valueOf(account.getDagligtMal()));

        saveButton = new Button("Spara");
        deleteButton = new Button("Radera konto");

        saveButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6;");
        deleteButton.setStyle("-fx-background-color: lightgray; -fx-background-radius: 6;");

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

        VBox centeredCard = new VBox(layout);
        centeredCard.setAlignment(Pos.CENTER);

        root.setTop(header);
        root.setCenter(centeredCard);
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

            String lösenAttSpara = nyttLösen.isEmpty() ? originalAccount.getLösenord() : nyttLösen;

<<<<<<< Updated upstream
        view.getCalorieLogButton().setOnAction(e -> {
            CalorieLogScreen screen = new CalorieLogScreen();
            Scene scene = new Scene(screen.getRoot());
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();// Använd faktisk storlek
            primaryStage.setResizable(false);  // Förhindra skeva ändringar
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Logga kalorier");
        });


        view.getLoggaUtButton().setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logga ut");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Vill du logga ut?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
=======
            Account uppdateratAccount = new Account(
                    originalAccount.getKontoID(),
                    nyttNamn,
                    nyttEfternamn,
                    nyttEpost,
                    lösenAttSpara,
                    originalAccount.getÅlder(),
                    nyVikt,
                    nyttKön,
                    nyttMål
            );

            boolean lyckades = new AccountDAO().uppdateraKonto(uppdateratAccount);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Profiluppdatering");
            alert.setHeaderText(null);
            alert.setContentText(lyckades ? "Profilen har uppdaterats." : "Misslyckades att spara ändringar.");
            alert.showAndWait();

            if (lyckades) {
                SessionManager.setUsername(nyttNamn);
                MainMenuScreen menu = new MainMenuScreen(nyttNamn);
                new MainMenuController(menu, primaryStage);
                primaryStage.setScene(new Scene(menu.getRoot(), 800, 600));
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
                boolean raderat = new AccountDAO().raderaKonto(originalAccount.getKontoID());
                Alert resultat = new Alert(Alert.AlertType.INFORMATION);
                resultat.setHeaderText(null);
                resultat.setContentText(raderat ? "Konto har raderats." : "Kunde inte radera konto.");
                resultat.showAndWait();

                if (raderat) {
>>>>>>> Stashed changes
                    SessionManager.clearAktivtKontoID();
                    SessionManager.setUsername(null);
                    Main.visaStartScreen(primaryStage);
                }
<<<<<<< Updated upstream
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
                primaryStage.centerOnScreen();
            } else {
                System.out.println("Konto hittades inte.");
=======
>>>>>>> Stashed changes
            }
        });
    }

    public Parent getRoot() {
        return root;
    }

    public void visaFönster() {
        Scene scene = new Scene(getRoot(), 800, 600);
        primaryStage.setTitle("Redigera Profil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
