package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.FitnessApp1.model.KaloriLogg;
import org.FitnessApp1.model.KaloriLoggDAO;
import org.FitnessApp1.model.SessionManager;
import java.time.LocalDate;
import java.util.List;

public class KaloriLoggScreen {

    private VBox layout;
    private TextField matField;
    private TextField kalorierField;
    private DatePicker datumPicker;
    private Button sparaButton;
    private Text dagensSummaText;
    private ListView<String> loggLista;
    private ProgressBar kaloriProgressBar;  // ProgressBar för kalorimål
    private Text procentText;  // För att visa procenten av målet
    private HBox kaloriProgressBox; // Lägg till HBox för att placera procent bredvid linjen

    public KaloriLoggScreen() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        matField = new TextField();
        kalorierField = new TextField();
        datumPicker = new DatePicker(LocalDate.now());
        sparaButton = new Button("Spara intag");
        dagensSummaText = new Text();
        loggLista = new ListView<>();
        kaloriProgressBar = new ProgressBar(0);  // Startvärde för ProgressBar
        procentText = new Text();  // Text för att visa procent
        kaloriProgressBox = new HBox(10); // Lägg till en HBox för att hålla ProgressBar och Procent

        // Visa dagens totala kalorier och lista med måltider
        uppdateraDagensSumma();
        uppdateraLoggLista();

        // Högerklicksmeny för redigering och borttagning
        ContextMenu contextMenu = new ContextMenu();
        MenuItem redigeraItem = new MenuItem("Redigera");
        MenuItem taBortItem = new MenuItem("Ta bort");
        contextMenu.getItems().addAll(redigeraItem, taBortItem);

        // Högerklicka för att redigera eller ta bort logg
        loggLista.setContextMenu(contextMenu);

        // Redigera logg
        redigeraItem.setOnAction(event -> {
            String selectedLogg = loggLista.getSelectionModel().getSelectedItem();
            if (selectedLogg != null) {
                String[] delar = selectedLogg.split(" \\(");
                String beskrivning = delar[0];
                int kalorier = Integer.parseInt(delar[1].replace(" kcal)", "").trim());

                // Öppna en dialog för att redigera
                TextInputDialog dialog = new TextInputDialog(beskrivning + ";" + kalorier);
                dialog.setTitle("Redigera logg");
                dialog.setHeaderText("Redigera beskrivning och kalorier (separera med semikolon)");
                dialog.setContentText("Exempel: Kyckling med ris;600");

                dialog.showAndWait().ifPresent(input -> {
                    try {
                        String[] newValues = input.split(";");
                        String newDescription = newValues[0].trim();
                        int newCalories = Integer.parseInt(newValues[1].trim());

                        // Uppdatera loggen
                        KaloriLoggDAO dao = new KaloriLoggDAO();
                        int kontoID = SessionManager.getAktivtKontoID();
                        KaloriLogg logg = new KaloriLogg(LocalDate.now(), newDescription, newCalories, kontoID);
                        dao.updateLogs(logg);

                        uppdateraLoggLista();  // Uppdatera listan efter ändring
                        uppdateraDagensSumma();  // Uppdatera kalorimål
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Felaktigt format. Använd semikolon för att separera beskrivning och kalorier.");
                        alert.showAndWait();
                    }
                });
            }
        });

        // Ta bort logg
        taBortItem.setOnAction(event -> {
            String selectedLogg = loggLista.getSelectionModel().getSelectedItem();
            if (selectedLogg != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Bekräfta borttagning");
                confirmAlert.setContentText("Vill du verkligen ta bort denna logg?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Ta bort loggen
                        KaloriLoggDAO dao = new KaloriLoggDAO();
                        int loggID = Integer.parseInt(selectedLogg.split(" \\(")[0]); // Extrahera loggID
                        dao.deleteLog(loggID);

                        uppdateraLoggLista();  // Uppdatera listan efter borttagning
                        uppdateraDagensSumma();  // Uppdatera kalorimål
                    }
                });
            }
        });

        layout.getChildren().addAll(
                new Label("Dagens totala kalorier:"), dagensSummaText,
                new Label("Loggar för idag:"), loggLista,
                new Label("Vad har du ätit?"), matField,
                new Label("Kalorier:"), kalorierField,
                new Label("Datum:"), datumPicker,
                sparaButton,
                new Label("ProgressBar för kaloriintag:"),
                kaloriProgressBox,  // Lägg till ProgressBar till layouten
                procentText // Lägg till procenttext för att visa hur mycket av målet som är uppnått
        );

        kaloriProgressBox.getChildren().addAll(kaloriProgressBar, procentText); // Lägg ProgressBar och procent i HBox

        sparaButton.setOnAction(event -> {
            try {
                String mat = matField.getText();
                int kalorier = Integer.parseInt(kalorierField.getText());
                LocalDate datum = datumPicker.getValue();

                int kontoID = SessionManager.getAktivtKontoID();
                if (kontoID == -1) throw new IllegalStateException("Ingen användare inloggad");

                KaloriLogg logg = new KaloriLogg(datum, mat, kalorier, kontoID);
                KaloriLoggDAO dao = new KaloriLoggDAO();

                // Spara kalorier
                boolean sparat = dao.addLog(logg);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Resultat");
                alert.setHeaderText(null);
                alert.setContentText(sparat
                        ? " Sparat: " + mat + " (" + kalorier + " kcal)"
                        : " Kunde inte spara.");
                alert.showAndWait();

                // Uppdatera listor och totalsumma
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
    }

    public Parent getRoot() {
        return layout;
    }

    // Uppdatera dagens totala kalorier samt progressbar
    private void uppdateraDagensSumma() {
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) {
            dagensSummaText.setText("Inte inloggad");
            kaloriProgressBar.setProgress(0); // Sätt progressbar till 0 om inte inloggad
            procentText.setText("0%");  // Sätt procenten till 0 om inte inloggad
            return;
        }

        KaloriLoggDAO dao = new KaloriLoggDAO();
        int total = dao.countTotalCalories(LocalDate.now(), kontoID);
        int dagligtMal = 2500;  // Justera till användarens mål om det behövs
        int kvarTillMal = dagligtMal - total;

        dagensSummaText.setText("Totalt: " + total + " kcal\nKvar till mål: " + kvarTillMal + " kcal");

        // Uppdatera ProgressBar med procent av dagens mål
        double progress = (double) total / dagligtMal;  // Beräkna hur mycket procent av målet som är uppnått
        kaloriProgressBar.setProgress(progress);

        // Uppdatera procenten som text
        int procent = (int) (progress * 100);
        procentText.setText(procent + "%");
    }

    private void uppdateraLoggLista() {
        loggLista.getItems().clear();
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        KaloriLoggDAO dao = new KaloriLoggDAO();
        List<KaloriLogg> loggar = dao.getLogsForDate(LocalDate.now(), kontoID);
        for (KaloriLogg logg : loggar) {
            loggLista.getItems().add(logg.getBeskrivning() + " (" + logg.getKalorier() + " kcal)");
        }
    }
}