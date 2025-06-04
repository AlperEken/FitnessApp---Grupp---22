package org.FitnessApp1.view;

import com.calendarfx.view.CalendarView;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.FitnessApp1.Main;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.CalendarDAO;
import org.FitnessApp1.model.SessionManager;

import java.time.LocalDate;

public class CalenderScreen {

    private final CalendarView calendarView;
    private final CalendarDAO calendarDAO;
    private Label selectedDateLabel;
    private final int kontoid;
    private VBox root;

    public CalenderScreen(int kontoid) {
        this.kontoid = kontoid;
        this.calendarDAO = new CalendarDAO();

        this.calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowPageSwitcher(true);
        calendarView.setShowPrintButton(false);
        calendarView.setShowDeveloperConsole(false);
        calendarView.showMonthPage();

        selectedDateLabel = new Label("Valt datum: -");

        calendarView.dateProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                selectedDateLabel.setText("Valt datum: " + newDate);
            }
        });

        // === Hemknapp (ikon) ===
        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeIcon.setOnMouseClicked(e -> {
            MainMenuScreen menuScreen = new MainMenuScreen(SessionManager.getUsername());
            new MainMenuController(menuScreen, Main.getPrimaryStage());
            Main.getPrimaryStage().setScene(new Scene(menuScreen.getRoot(), 800, 600));
        });;
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));

        HBox topBox = new HBox(10, homeIcon, selectedDateLabel);
        topBox.setPadding(new Insets(10));

        Button btnNyAnteckning = new Button("Ny anteckning");
        btnNyAnteckning.setOnAction(e -> {
            LocalDate date = calendarView.getDate();
            if (date != null) {
                visaFönster(date);
            }
        });

        Button btnVisaAnteckning = new Button("Visa anteckning");
        btnVisaAnteckning.setOnAction(e -> {
            LocalDate date = calendarView.getDate();
            if (date != null) {
                String note = calendarDAO.getNoteForDate(date, kontoid);
                if (note != null && !note.isEmpty()) {
                    visaFönster(date);
                } else {
                    visaInfo("Ingen anteckning för datum: " + date);
                }
            }
        });

        Button btnTaBortAnteckning = new Button("Ta bort anteckning");
        btnTaBortAnteckning.setOnAction(e -> {
            LocalDate date = calendarView.getDate();
            if (date != null) {
                String note = calendarDAO.getNoteForDate(date, kontoid);
                if (note != null && !note.isEmpty()) {
                    boolean success = calendarDAO.deleteNote(date, kontoid);
                    if (success) {
                        visaInfo("Anteckningen för " + date + " är borttagen.");
                    } else {
                        visaInfo("Kunde inte ta bort anteckningen.");
                    }
                } else {
                    visaInfo("Ingen anteckning att ta bort för datum: " + date);
                }
            }
        });

        HBox buttonsBox = new HBox(10, btnNyAnteckning, btnVisaAnteckning, btnTaBortAnteckning);
        buttonsBox.setPadding(new Insets(10));

        root = new VBox(10, topBox, buttonsBox, calendarView);
        root.setPadding(new Insets(10));
    }

    public CalendarView getCalendarView() {
        return calendarView;
    }

    public VBox getRoot() {
        return root;
    }

    public void visaFönster(LocalDate datum) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Anteckning för " + datum);

        Label label = new Label("Datum: " + datum);
        TextArea anteckningArea = new TextArea();
        anteckningArea.setWrapText(true);

        String existingNote = calendarDAO.getNoteForDate(datum, kontoid);
        if (existingNote != null) {
            anteckningArea.setText(existingNote);
        }

        Button btnSpara = new Button("Spara");
        Button btnAvbryt = new Button("Avbryt");

        btnSpara.setOnAction(e -> {
            String text = anteckningArea.getText().trim();
            boolean success;
            if (!text.isEmpty()) {
                success = calendarDAO.saveOrUpdate(datum, kontoid, text);
            } else {
                success = calendarDAO.deleteNote(datum, kontoid);
            }
            if (success) {
                stage.close();
                visaInfo("Anteckningen för " + datum + " har sparats.");
            } else {
                visaInfo("Kunde inte spara anteckningen. Försök igen.");
            }
        });

        btnAvbryt.setOnAction(e -> stage.close());

        VBox layout = new VBox(10, label, anteckningArea, btnSpara, btnAvbryt);
        layout.setPadding(new Insets(10));

        stage.setScene(new Scene(layout, 400, 300));
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
