package org.FitnessApp1.view;

import org.FitnessApp1.Main;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.ActivityDAO;
import org.FitnessApp1.model.CalendarDAO;
import org.FitnessApp1.model.NoteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.FitnessApp1.model.Activity;
import org.FitnessApp1.model.Note;
import org.FitnessApp1.model.SessionManager;
import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CalendarScreen {
    private CalendarView calendarView;
    private CalendarDAO calendarDAO;
    private ActivityDAO activityDAO;
    private NoteDAO noteDAO;
    private Calendar activityCalendar;

    public CalendarScreen() {
        calendarDAO = new CalendarDAO();
        activityDAO = new ActivityDAO();
        noteDAO = new NoteDAO();
    }

    public Scene getScene() {
        calendarView = new CalendarView();
        calendarView.showToolBarProperty().set(true);
        calendarView.setDate(LocalDate.now());

        activityCalendar = new Calendar("Mina aktiviteter");
        calendarView.getCalendarSources().get(0).getCalendars().add(activityCalendar);
        loadActivitiesToCalendarView();

        Button addActivityButton = new Button("Ny aktivitet");
        Button addNoteButton = new Button("Ny anteckning");
        Button viewNotesButton = new Button("Visa anteckningar");

        addActivityButton.setOnAction(e -> showAddActivityWindow());
        addNoteButton.setOnAction(e -> showAddNoteWindow());
        viewNotesButton.setOnAction(e -> showViewNotesWindow());

        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitHeight(30);
        homeIcon.setFitWidth(30);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> {
            MainMenuScreen menuScreen = new MainMenuScreen(SessionManager.getUsername());
            new MainMenuController(menuScreen, Main.getPrimaryStage());
            Main.getPrimaryStage().setScene(new Scene(menuScreen.getRoot(), 800, 600));
        });
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));

        HBox topBar = new HBox(15, homeIcon, addActivityButton, addNoteButton, viewNotesButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(calendarView);

        return new Scene(root, 1000, 700);
    }

    private void loadActivitiesToCalendarView() {
        activityCalendar.clear();
        LocalDate current = calendarView.getDate();
        List<Activity> activities = activityDAO.getActivitiesForDay(SessionManager.getAktivtKontoID(), current);
        for (Activity a : activities) {
            Entry<String> entry = new Entry<>(a.getNote());
            entry.changeStartDate(a.getDate());
            entry.changeStartTime(a.getStartTime());
            entry.changeEndDate(a.getDate());
            entry.changeEndTime(a.getEndTime());
            activityCalendar.addEntry(entry);
        }
    }

    private void showAddActivityWindow() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Ny aktivitet");

        TextField noteField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField startTimeField = new TextField("08:00");
        TextField endTimeField = new TextField("09:00");

        Button saveButton = new Button("Spara aktivitet");
        saveButton.setOnAction(e -> {
            String note = noteField.getText();
            LocalDate date = datePicker.getValue();
            LocalTime start = LocalTime.parse(startTimeField.getText());
            LocalTime end = LocalTime.parse(endTimeField.getText());
            int kalenderId = SessionManager.getAktivtKontoID();

            boolean success = activityDAO.saveActivity(note, date, start, end, kalenderId);

            if (success) {
                showPopup("Aktivitet sparad!");
                window.close();
                calendarView.setDate(date); // Hoppa till det datum användaren valde
                loadActivitiesToCalendarView();
            } else {
                showPopup("Fel vid sparning! Kontrollera kalenderID och databasanslutning.");
            }
        });

        VBox layout = new VBox(10, new Label("Anteckning:"), noteField,
                new Label("Datum:"), datePicker,
                new Label("Starttid (HH:mm):"), startTimeField,
                new Label("Sluttid (HH:mm):"), endTimeField,
                saveButton);
        layout.setPadding(new Insets(10));

        window.setScene(new Scene(layout));
        window.showAndWait();
    }

    private void showAddNoteWindow() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Ny anteckning");

        TextArea textArea = new TextArea();
        Button saveButton = new Button("Spara anteckning");

        saveButton.setOnAction(e -> {
            Note note = new Note();
            note.setContent(textArea.getText());
            note.setKontoId(SessionManager.getAktivtKontoID());
            noteDAO.saveNote(note);
            window.close();
        });

        VBox layout = new VBox(10, new Label("Anteckning:"), textArea, saveButton);
        layout.setPadding(new Insets(10));

        window.setScene(new Scene(layout));
        window.showAndWait();
    }

    private void showViewNotesWindow() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Mina anteckningar");

        List<Note> notes = noteDAO.getNotesByKontoId(SessionManager.getAktivtKontoID());
        ObservableList<Note> observableNotes = FXCollections.observableArrayList(notes);
        ListView<Note> listView = new ListView<>(observableNotes);

        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Note> call(ListView<Note> param) {
                ListCell<Note> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Note note, boolean empty) {
                        super.updateItem(note, empty);
                        if (note != null && !empty) {
                            setText(note.getContent());
                        } else {
                            setText(null);
                        }
                    }
                };

                cell.setOnMouseClicked(event -> {
                    if (!cell.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                        Note selectedNote = cell.getItem();

                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem editItem = new MenuItem("Redigera");
                        MenuItem deleteItem = new MenuItem("Ta bort");

                        editItem.setOnAction(ev -> showEditNoteWindow(selectedNote, observableNotes));
                        deleteItem.setOnAction(ev -> {
                            noteDAO.deleteNoteById(selectedNote.getId());
                            observableNotes.remove(selectedNote);
                        });

                        contextMenu.getItems().addAll(editItem, deleteItem);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                });
                return cell;
            }
        });

        VBox layout = new VBox(10, listView);
        layout.setPadding(new Insets(10));
        window.setScene(new Scene(layout, 400, 400));
        window.showAndWait();
    }

    private void showEditNoteWindow(Note note, ObservableList<Note> noteList) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Redigera anteckning");

        TextArea textArea = new TextArea(note.getContent());
        Button saveButton = new Button("Spara ändringar");

        saveButton.setOnAction(e -> {
            note.setContent(textArea.getText());
            noteDAO.updateNote(note);
            window.close();
            noteList.setAll(noteDAO.getNotesByKontoId(SessionManager.getAktivtKontoID()));
        });

        VBox layout = new VBox(10, new Label("Ändra anteckning:"), textArea, saveButton);
        layout.setPadding(new Insets(10));
        window.setScene(new Scene(layout));
        window.showAndWait();
    }

    private void showPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bekräftelse");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
