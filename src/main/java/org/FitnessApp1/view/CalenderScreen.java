package org.FitnessApp1.view;

import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.FitnessApp1.model.CalendarDAO;

import java.time.LocalDate;

public class CalenderScreen {

    private final CalendarView calendarView;
    private final CalendarDAO calendarDAO;
    private Label selectedDateLabel;

    public CalenderScreen() {
        calendarDAO = new CalendarDAO();

        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowPageSwitcher(true);
        calendarView.setShowPrintButton(false);
        calendarView.setShowDeveloperConsole(false);

        calendarView.showMonthPage();

        selectedDateLabel = new Label("Valt datum: -");

        // Uppdatera label när datum ändras
        calendarView.dateProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                selectedDateLabel.setText("Valt datum: " + newDate);
            }
        });

        // Knapp för att skapa ny anteckning på valt datum
        Button btnNyAnteckning = new Button("Ny anteckning");
        btnNyAnteckning.setOnAction(e -> {
            LocalDate date = calendarView.getDate();
            if (date != null) {
                visaFönster(date);
            }
        });

        // Knapp för att visa befintlig anteckning (om någon) på valt datum
        Button btnVisaAnteckning = new Button("Visa anteckning");
        btnVisaAnteckning.setOnAction(e -> {
            LocalDate date = calendarView.getDate();
            if (date != null) {
                String note = calendarDAO.getNoteForDate(date);
                if (note != null && !note.isEmpty()) {
                    visaFönster(date);
                } else {
                    visaInfo("Ingen anteckning för datum: " + date);
                }
            }
        });

        // Ny knapp för att ta bort anteckning
        Button btnTaBortAnteckning = new Button("Ta bort anteckning");
        btnTaBortAnteckning.setOnAction(e -> {
            LocalDate date = calendarView.getDate();
            if (date != null) {
                String note = calendarDAO.getNoteForDate(date);
                if (note != null && !note.isEmpty()) {
                    calendarDAO.deleteNote(date);
                    visaInfo("Anteckningen för " + date + " är borttagen.");
                } else {
                    visaInfo("Ingen anteckning att ta bort för datum: " + date);
                }
            }
        });

        HBox buttonsBox = new HBox(10, btnNyAnteckning, btnVisaAnteckning, btnTaBortAnteckning);
        buttonsBox.setPadding(new Insets(10));

        VBox root = new VBox(10, selectedDateLabel, buttonsBox, calendarView);
        root.setPadding(new Insets(10));

        // Skapa och visa scenen (om du vill visa i egen scen)
        Stage stage = new Stage();
        stage.setTitle("Kalender");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /** Returnerar kalender-noden för inbäddning (om du vill bädda in i annan scen) */
    public CalendarView getCalendarView() {
        return calendarView;
    }

    public void visaFönster(LocalDate datum) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Anteckning för " + datum);

        Label label = new Label("Datum: " + datum);

        TextArea anteckningArea = new TextArea();
        anteckningArea.setWrapText(true);

        String existingNote = calendarDAO.getNoteForDate(datum);
        if (existingNote != null) {
            anteckningArea.setText(existingNote);
        }

        Button btnSpara = new Button("Spara");
        Button btnAvbryt = new Button("Avbryt");

        btnSpara.setOnAction(e -> {
            String text = anteckningArea.getText().trim();
            if (!text.isEmpty()) {
                calendarDAO.saveOrUpdate(datum, text);
            } else {
                calendarDAO.deleteNote(datum);
            }
            stage.close();
        });

        btnAvbryt.setOnAction(e -> stage.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(label, anteckningArea, btnSpara, btnAvbryt);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void visaInfo(String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Information");

        Label label = new Label(message);
        Button btnOk = new Button("OK");
        btnOk.setOnAction(e -> stage.close());

        VBox layout = new VBox(10, label, btnOk);
        layout.setPadding(new Insets(10));

        stage.setScene(new Scene(layout, 300, 150));
        stage.showAndWait();
    }
}
