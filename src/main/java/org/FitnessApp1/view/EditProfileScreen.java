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

        // === Root med gradientbakgrund ===
        root = new BorderPane();
        root.setStyle("""
    -fx-background-color: linear-gradient(
        from 0% 0% to 100% 100%,
        #26c6da,  /* turkos */
        #00838f,  /* petrolblå */
        #283593   /* djupblå */
    );
""");
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.setMaxWidth(400);
        layout.setStyle("""
    -fx-background-color: white;
    -fx-background-radius: 12px;
    -fx-border-radius: 12px;
    -fx-border-color: #e0e0e0;
    -fx-border-width: 1;
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0.2, 0, 2);
""");

        // === Header ===
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 30, 10, 30));

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
        title.setFont(Font.font("Italic", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #1A3E8B;");
        Label subtitle = new Label("Uppdatera din profilinformation.");
        subtitle.setFont(Font.font("italic", 14));
        subtitle.setStyle("-fx-text-fill: #555;");
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(homeIcon, titleBox);

        // === Fält med placeholders ===
        nameField = new TextField(account.getName());
        nameField.setPromptText("Exempel: Anna");

        efternameField = new TextField(account.getLastname());
        efternameField.setPromptText("Exempel: Svensson");

        emailField = new TextField(account.getEmail());
        emailField.setPromptText("Exempel: anna@mail.com");

        passwordField = new PasswordField();
        passwordField.setPromptText("Lämna tomt om du inte vill ändra");

        weightField = new TextField(String.valueOf(account.getWeight()));
        weightField.setPromptText("Exempel: 70");

        genderField = new TextField(account.getGender());
        genderField.setPromptText("Exempel: Kvinna");

        goalField = new TextField(String.valueOf(account.getDaliyGoals()));
        goalField.setPromptText("Exempel: 2000");

        // === Knappar ===
        saveButton = new Button("Spara");
        deleteButton = new Button("Radera konto");

        saveButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #0F2A5C; -fx-text-fill: white; -fx-background-radius: 6;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6;"));

        deleteButton.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #B71C1C; -fx-text-fill: white; -fx-background-radius: 6;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-background-radius: 6;"));

        // === Lägg till alla fält ===
        layout.getChildren().addAll(
                createFieldLabel("Förnamn:"), nameField,
                createFieldLabel("Efternamn:"), efternameField,
                createFieldLabel("E-post:"), emailField,
                createFieldLabel("Lösenord:"), passwordField,
                createFieldLabel("Vikt (kg):"), weightField,
                createFieldLabel("Kön:"), genderField,
                createFieldLabel("Dagligt mål (kalorier):"), goalField,
                saveButton,
                deleteButton
        );

        saveButton.setOnAction(e -> saveChanges());
        deleteButton.setOnAction(e -> eraseAccount());


        VBox centeredCard = new VBox(layout);
        centeredCard.setAlignment(Pos.CENTER);
        centeredCard.setPadding(new Insets(30));
        centeredCard.setStyle("-fx-background-color: transparent;");

        root.setTop(header);
        root.setCenter(centeredCard);

    }

    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: black;");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        return label;
    }

    private void saveChanges() {
        try {
            String nyttNamn = nameField.getText();
            String nyttEfternamn = efternameField.getText();
            String nyttEpost = emailField.getText();
            String nyttLösen = passwordField.getText();
            double nyVikt = Double.parseDouble(weightField.getText());
            String nyttKön = genderField.getText();
            int nyttMål = Integer.parseInt(goalField.getText());

            String lösenAttSpara = nyttLösen.isEmpty() ? originalAccount.getPassword() : nyttLösen;

            Account uppdateratAccount = new Account(
                    originalAccount.getAccountID(),
                    nyttNamn,
                    nyttEfternamn,
                    nyttEpost,
                    lösenAttSpara,
                    originalAccount.getAge(),
                    nyVikt,
                    nyttKön,
                    nyttMål
            );

            boolean lyckades = new AccountDAO().updateAccount(uppdateratAccount);

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

    private void eraseAccount() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Bekräftelse");
        confirm.setHeaderText("Är du säker?");
        confirm.setContentText("Detta kommer att permanent ta bort ditt konto.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean raderat = new AccountDAO().eraseAccount(originalAccount.getAccountID());
                Alert resultat = new Alert(Alert.AlertType.INFORMATION);
                resultat.setHeaderText(null);
                resultat.setContentText(raderat ? "Konto har raderats." : "Kunde inte radera konto.");
                resultat.showAndWait();

                if (raderat) {
                    SessionManager.clearActiveAccountID();
                    SessionManager.setUsername(null);
                    Main.showStartScreen(primaryStage);
                }
            }
        });
    }

    public Parent getRoot() {
        return root;
    }

}
