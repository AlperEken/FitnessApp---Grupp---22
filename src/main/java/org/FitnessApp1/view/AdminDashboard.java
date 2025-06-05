package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.FitnessApp1.model.Account;
import org.FitnessApp1.model.AccountDAO;
import org.FitnessApp1.model.AdminDAO;

public class AdminDashboard {

    private final Stage stage;

    public AdminDashboard(Stage stage) {
        this.stage = stage;
    }

    public void view() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        Button btnAddAdmin = new Button("Lägg till admin");
        Button btnRemoveAdmin = new Button("Ta bort admin");
        Button btnDeleteAccount = new Button("Ta bort konto");
        Button btnResetPassword = new Button("Återställ lösenord");

        btnAddAdmin.setOnAction(e -> addNewAdmin());
        btnRemoveAdmin.setOnAction(e -> removeAdmin());
        btnDeleteAccount.setOnAction(e -> removeAccount());
        btnResetPassword.setOnAction(e -> resetPassword());

        layout.getChildren().addAll(btnAddAdmin, btnRemoveAdmin, btnDeleteAccount, btnResetPassword);

        Scene scene = new Scene(layout, 400, 300);
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
                String nyttLösen = delar[1].trim();

                AccountDAO dao = new AccountDAO();
                int kontoID = dao.getAccountIDByEmail(epost);

                if (kontoID == -1) {
                    showError("Inget konto hittades med e-post: " + epost);
                    return;
                }

                Account konto = dao.getAccountByID(kontoID);
                if (konto != null) {
                    konto.setLösenord(nyttLösen);
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

    private void showResult(boolean lyckades, String okMeddelande, String felMeddelande) {
        Alert alert = new Alert(lyckades ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(lyckades ? "Klart" : "Fel");
        alert.setHeaderText(null);
        alert.setContentText(lyckades ? okMeddelande : felMeddelande);
        alert.showAndWait();
    }

    private void showError(String felmeddelande) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fel");
        alert.setContentText(felmeddelande);
        alert.showAndWait();
    }
}
