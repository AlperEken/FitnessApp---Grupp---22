package org.FitnessApp1.view;

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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lägg till admin");
        dialog.setHeaderText("Fyll i e-post för ett befintligt konto");

        dialog.showAndWait().ifPresent(epost -> {
            boolean skapad = new AdminDAO().createAdmin(epost.trim());
            showResult(skapad, "Admin tillagd", "Misslyckades lägga till admin (kanske finns redan?)");
        });
    }

    private void removeAdmin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ta bort admin");
        dialog.setHeaderText("Fyll i adminens e-post att ta bort");

        dialog.showAndWait().ifPresent(email -> {
            boolean raderad = new AdminDAO().removeAdmin(email.trim());
            showResult(raderad, "Admin raderad", "Kunde inte radera admin");
        });
    }

    private void removeAccount() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ta bort konto");
        dialog.setHeaderText("Fyll i e-post för kontot du vill radera");

        dialog.showAndWait().ifPresent(epost -> {
            AccountDAO dao = new AccountDAO();
            int kontoID = dao.getAccountIDByEmail(epost.trim());

            if (kontoID == -1) {
                showError("Inget konto hittades med e-post: " + epost);
                return;
            }

            boolean raderat = dao.raderaKonto(kontoID);
            showResult(raderat, "Konto raderat", "Kunde inte radera kontot");
        });
    }

    private void resetPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Återställ lösenord");
        dialog.setHeaderText("Fyll i e-post och nytt lösenord (separera med komma)");

        dialog.showAndWait().ifPresent(input -> {
            String[] delar = input.split(",");
            if (delar.length == 2) {
                String epost = delar[0].trim();
                String nyttLosen = delar[1].trim();

                AccountDAO dao = new AccountDAO();
                int kontoID = dao.getAccountIDByEmail(epost);

                if (kontoID == -1) {
                    showError("Inget konto hittades med e-post: " + epost);
                    return;
                }

                Account konto = dao.getAccountByID(kontoID);
                if (konto != null) {
                    konto.setLösenord(nyttLosen);
                    boolean uppdaterat = dao.uppdateraKonto(konto);
                    showResult(uppdaterat, "Lösenord uppdaterat", "Kunde inte uppdatera lösenord");
                } else {
                    showError("Konto kunde inte hämtas.");
                }
            } else {
                showError("Du måste skriva e-post och nytt lösenord separerat med komma.");
            }
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
