package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.FitnessApp1.model.KaloriLogg;
import org.FitnessApp1.model.KaloriLoggDAO;
import org.FitnessApp1.model.Konto;
import org.FitnessApp1.model.KontoDAO;
import org.FitnessApp1.model.SessionManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KaloriLoggScreen {

    private VBox layout;
    private TextField matField;
    private TextField kalorierField;
    private DatePicker datumPicker;
    private Button sparaButton;
    private Text dagensSummaText;
    private ListView<String> loggLista;
    private ProgressBar kaloriProgressBar;
    private Text procentText;
    private HBox kaloriProgressBox;
    private Map<String, Integer> loggTextTillID = new HashMap<>();

    public KaloriLoggScreen() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        matField = new TextField();
        kalorierField = new TextField();
        datumPicker = new DatePicker(LocalDate.now());
        sparaButton = new Button("Spara intag");
        dagensSummaText = new Text();
        loggLista = new ListView<>();
        kaloriProgressBar = new ProgressBar(0);
        procentText = new Text();
        kaloriProgressBox = new HBox(10);

        uppdateraDagensSumma();
        uppdateraLoggLista();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem redigeraItem = new MenuItem("Redigera");
        MenuItem taBortItem = new MenuItem("Ta bort");
        contextMenu.getItems().addAll(redigeraItem, taBortItem);
        loggLista.setContextMenu(contextMenu);

        redigeraItem.setOnAction(event -> {
            String selectedLogg = loggLista.getSelectionModel().getSelectedItem();
            if (selectedLogg != null) {
                String[] delar = selectedLogg.split(" \\(");
                String beskrivning = delar[0];
                int kalorier = Integer.parseInt(delar[1].replace(" kcal)", "").trim());

                TextInputDialog dialog = new TextInputDialog(beskrivning + ";" + kalorier);
                dialog.setTitle("Redigera logg");
                dialog.setHeaderText("Redigera beskrivning och kalorier (separera med semikolon)");
                dialog.setContentText("Exempel: Kyckling med ris;600");

                dialog.showAndWait().ifPresent(input -> {
                    try {
                        String[] newValues = input.split(";");
                        String newDescription = newValues[0].trim();
                        int newCalories = Integer.parseInt(newValues[1].trim());

                        int kontoID = SessionManager.getAktivtKontoID();
                        int loggID = loggTextTillID.get(selectedLogg);
                        KaloriLogg logg = new KaloriLogg(loggID, datumPicker.getValue(), newDescription, newCalories, kontoID);
                        new KaloriLoggDAO().updateLogs(logg);

                        uppdateraLoggLista();
                        uppdateraDagensSumma();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Felaktigt format. Använd semikolon för att separera beskrivning och kalorier.");
                        alert.showAndWait();
                    }
                });
            }
        });

        taBortItem.setOnAction(event -> {
            String selectedLogg = loggLista.getSelectionModel().getSelectedItem();
            if (selectedLogg != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Bekräfta borttagning");
                confirmAlert.setContentText("Vill du verkligen ta bort denna logg?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int loggID = loggTextTillID.get(selectedLogg);
                        new KaloriLoggDAO().deleteLog(loggID);
                        uppdateraLoggLista();
                        uppdateraDagensSumma();
                    }
                });
            }
        });

        sparaButton.setOnAction(event -> {
            try {
                String mat = matField.getText();
                int kalorier = Integer.parseInt(kalorierField.getText());
                LocalDate datum = datumPicker.getValue();

                int kontoID = SessionManager.getAktivtKontoID();
                KaloriLogg logg = new KaloriLogg(datum, mat, kalorier, kontoID);
                boolean sparat = new KaloriLoggDAO().addLog(logg);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Resultat");
                alert.setHeaderText(null);
                alert.setContentText(sparat ? " Sparat: " + mat + " (" + kalorier + " kcal)" : " Kunde inte spara.");
                alert.showAndWait();

                uppdateraDagensSumma();
                uppdateraLoggLista();
                matField.clear();
                kalorierField.clear();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fel");
                alert.setContentText("Fyll i både vad du ätit och kalorier (som siffra).");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(
                new Label("Dagens totala kalorier:"), dagensSummaText,
                new Label("Loggar för valt datum:"), loggLista,
                new Label("Vad har du ätit?"), matField,
                new Label("Kalorier:"), kalorierField,
                new Label("Datum:"), datumPicker,
                sparaButton,
                new Label("ProgressBar för kaloriintag:"),
                kaloriProgressBox,
                procentText
        );

        kaloriProgressBox.getChildren().addAll(kaloriProgressBar, procentText);
    }

    public Parent getRoot() {
        return layout;
    }

    private void uppdateraDagensSumma() {
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) {
            dagensSummaText.setText("Inte inloggad");
            kaloriProgressBar.setProgress(0);
            procentText.setText("0%");
            return;
        }

        LocalDate datum = datumPicker.getValue();
        KaloriLoggDAO dao = new KaloriLoggDAO();
        int total = dao.countTotalCalories(datum, kontoID);

        KontoDAO kontoDAO = new KontoDAO();
        Konto konto = kontoDAO.getAccountByID(kontoID);
        int dagligtMal = konto.getDagligtMal();

        int kvarTillMal = dagligtMal - total;
        dagensSummaText.setText("Totalt: " + total + " kcal\nKvar till mål: " + kvarTillMal + " kcal");

        double progress = (double) total / dagligtMal;
        kaloriProgressBar.setProgress(progress);
        int procent = (int) (progress * 100);
        procentText.setText(procent + "%");
    }

    private void uppdateraLoggLista() {
        loggLista.getItems().clear();
        loggTextTillID.clear();

        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        LocalDate datum = datumPicker.getValue();
        KaloriLoggDAO dao = new KaloriLoggDAO();
        List<KaloriLogg> loggar = dao.getLogsForDate(datum, kontoID);

        for (KaloriLogg logg : loggar) {
            String text = logg.getBeskrivning() + " (" + logg.getKalorier() + " kcal)";
            loggLista.getItems().add(text);
            loggTextTillID.put(text, logg.getLoggID());
        }
    }
}
