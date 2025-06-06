package org.FitnessApp1.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.FitnessApp1.model.*;

public class AdminDashboard {

    private final Stage stage;
    private final LoginScreen loginScreen;
    private final AccountDAO accountDAO = new AccountDAO();


    public AdminDashboard(Stage stage, LoginScreen loginScreen) {
        this.stage = stage;
        this.loginScreen = loginScreen;


    }

    public void view() {
        BorderPane root = new BorderPane();
        root.setStyle("""
            -fx-background-color: linear-gradient(
                from 0% 0% to 100% 100%,
                #26c6da,
                #00838f,
                #283593
            );
        """);

        VBox layoutCard = new VBox(15);
        layoutCard.setPadding(new Insets(30));
        layoutCard.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        layoutCard.setMaxWidth(350);
        layoutCard.setMinWidth(300);
        layoutCard.setAlignment(Pos.CENTER);

        Text title = new Text("Adminpanel");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1A3E8B;");

        Button btnAddAdmin = new Button("Lägg till admin");
        Button btnRemoveAdmin = new Button("Ta bort admin");
        Button btnDeleteAccount = new Button("Ta bort konto");
        Button btnResetPassword = new Button("Återställ lösenord");

        for (Button btn : new Button[]{btnAddAdmin, btnRemoveAdmin, btnDeleteAccount, btnResetPassword}) {
            btn.setPrefWidth(250);
            btn.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #0F2A5C; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;"));
        }

        btnAddAdmin.setOnAction(e -> addNewAdmin());
        btnRemoveAdmin.setOnAction(e -> removeAdmin());
        btnDeleteAccount.setOnAction(e -> removeAccount());
        btnResetPassword.setOnAction(e -> resetPassword());

        layoutCard.getChildren().addAll(title, btnAddAdmin, btnRemoveAdmin, btnDeleteAccount, btnResetPassword);

        VBox wrapper = new VBox(layoutCard);
        wrapper.setPadding(new Insets(50));
        wrapper.setAlignment(Pos.CENTER);

        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> {
            SessionManager.clearAktivtKontoID();
            SessionManager.setUsername(null);

            Scene loginScene = loginScreen.getRoot().getScene();
            if (loginScene != null) {
                stage.setScene(loginScene);
                stage.centerOnScreen();
            }
        });
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));

        root.setTop(homeIcon);
        BorderPane.setMargin(homeIcon, new Insets(20, 0, 0, 20));
        root.setCenter(wrapper);

        Scene scene = new Scene(root, 600, 550);
        stage.setScene(scene);
        stage.setTitle("Adminpanel");
        stage.centerOnScreen();
    }

    private void addNewAdmin() {
        AdminDAO adminDAO = new AdminDAO();

        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("Lägg till admin");
        dialog.setHeaderText("Välj ett konto att ge admin-rättigheter");

        ButtonType addButtonType = new ButtonType("Lägg till", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        ObservableList<Account> nonAdmins = adminDAO.getNonAdmins();
        nonAdmins.sort((a, b) -> a.getNamn().compareToIgnoreCase(b.getNamn()));

        ListView<Account> listView = new ListView<>(FXCollections.observableArrayList(nonAdmins));
        listView.setPrefHeight(250);

        TextField searchField = new TextField();
        searchField.setPromptText("Sök namn eller e-post...");

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String lower = newText.toLowerCase();
            ObservableList<Account> filtered = FXCollections.observableArrayList();

            for (Account acc : nonAdmins) {
                if (acc.getNamn().toLowerCase().contains(lower) ||
                        acc.getEfternamn().toLowerCase().contains(lower) ||
                        acc.getEpost().toLowerCase().contains(lower)) {
                    filtered.add(acc);
                }
            }

            listView.setItems(filtered);
        });

        VBox content = new VBox(10, searchField, listView);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == addButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selected -> {
            if (selected != null) {
                boolean added = adminDAO.createAdmin(selected.getEpost());
                showResult(added, "Admin tillagd", "Misslyckades lägga till admin.");
            } else {
                showError("Du måste välja ett konto.");
            }
        });
    }



    private void removeAdmin() {
        AdminDAO adminDAO = new AdminDAO();

        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("Ta bort admin");
        dialog.setHeaderText("Välj en admin att ta bort");

        ButtonType removeButtonType = new ButtonType("Ta bort", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, ButtonType.CANCEL);

        ObservableList<Account> admins = adminDAO.getAllAdmins();
        admins.sort((a, b) -> a.getNamn().compareToIgnoreCase(b.getNamn()));

        ListView<Account> listView = new ListView<>(FXCollections.observableArrayList(admins));
        listView.setPrefHeight(250);

        TextField searchField = new TextField();
        searchField.setPromptText("Sök admin via namn eller e-post...");

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String lower = newText.toLowerCase();
            ObservableList<Account> filtered = FXCollections.observableArrayList();

            for (Account acc : admins) {
                if (acc.getNamn().toLowerCase().contains(lower) ||
                        acc.getEfternamn().toLowerCase().contains(lower) ||
                        acc.getEpost().toLowerCase().contains(lower)) {
                    filtered.add(acc);
                }
            }

            listView.setItems(filtered);
        });

        VBox content = new VBox(10, searchField, listView);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == removeButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selected -> {
            if (selected != null) {
                boolean removed = adminDAO.removeAdmin(selected.getEpost());
                showResult(removed, "Admin borttagen", "Kunde inte ta bort admin.");
            } else {
                showError("Du måste välja en admin.");
            }
        });
    }



    private void removeAccount() {
        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("Ta bort konto");
        dialog.setHeaderText("Välj ett konto att ta bort");

        ButtonType removeButtonType = new ButtonType("Ta bort", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, ButtonType.CANCEL);

        ObservableList<Account> allAccounts = accountDAO.getAllaKonton();
        allAccounts.sort((a, b) -> a.getNamn().compareToIgnoreCase(b.getNamn()));

        ListView<Account> listView = new ListView<>(FXCollections.observableArrayList(allAccounts));
        listView.setPrefHeight(250);

        TextField searchField = new TextField();
        searchField.setPromptText("Sök efter namn eller e-post...");

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String lower = newText.toLowerCase();
            ObservableList<Account> filtered = FXCollections.observableArrayList();

            for (Account acc : allAccounts) {
                if (acc.getNamn().toLowerCase().contains(lower) ||
                        acc.getEfternamn().toLowerCase().contains(lower) ||
                        acc.getEpost().toLowerCase().contains(lower)) {
                    filtered.add(acc);
                }
            }

            listView.setItems(filtered);
        });

        VBox content = new VBox(10, searchField, listView);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == removeButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selected -> {
            if (selected != null) {
                boolean removed = accountDAO.raderaKonto(selected.getKontoID());
                showResult(removed, "Kontot togs bort", "Kunde inte radera kontot.");
            } else {
                showError("Du måste välja ett konto.");
            }
        });
    }




    private void resetPassword() {
        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("Återställ lösenord");
        dialog.setHeaderText("Välj ett konto och ange nytt lösenord");

        ButtonType resetButtonType = new ButtonType("Återställ", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resetButtonType, ButtonType.CANCEL);

        ObservableList<Account> allAccounts = accountDAO.getAllaKonton();
        allAccounts.sort((a, b) -> a.getNamn().compareToIgnoreCase(b.getNamn()));

        ListView<Account> listView = new ListView<>(FXCollections.observableArrayList(allAccounts));
        listView.setPrefHeight(200);

        TextField searchField = new TextField();
        searchField.setPromptText("Sök namn eller e-post...");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nytt lösenord");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            ObservableList<Account> filtered = FXCollections.observableArrayList();

            for (Account acc : allAccounts) {
                if (acc.getNamn().toLowerCase().contains(lower) ||
                        acc.getEfternamn().toLowerCase().contains(lower) ||
                        acc.getEpost().toLowerCase().contains(lower)) {
                    filtered.add(acc);
                }
            }

            listView.setItems(filtered);
        });

        VBox content = new VBox(10, searchField, listView, newPasswordField);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == resetButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selected -> {
            String newPassword = newPasswordField.getText();

            if (selected == null) {
                showError("Du måste välja ett konto.");
                return;
            }

            if (newPassword == null || newPassword.isBlank()) {
                showError("Lösenordet får inte vara tomt.");
                return;
            }

            selected.setLösenord(newPassword);
            boolean updated = accountDAO.uppdateraKonto(selected);
            showResult(updated, "Lösenord uppdaterat", "Kunde inte uppdatera lösenord.");
        });
    }


    private void showResult(boolean lyckades, String ok, String fel) {
        Alert alert = new Alert(lyckades ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(lyckades ? "Klart" : "Fel");
        alert.setHeaderText(null);
        alert.setContentText(lyckades ? ok : fel);
        alert.showAndWait();
    }

    private void showError(String felmeddelande) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fel");
        alert.setContentText(felmeddelande);
        alert.showAndWait();
    }
}
