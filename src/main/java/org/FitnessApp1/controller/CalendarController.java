package org.FitnessApp1.controller;

import javafx.concurrent.Task;
import javafx.scene.control.TextInputDialog;
import org.FitnessApp1.model.CalendarDAO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CalendarController {

    private static CalendarDAO calendarDAO = new CalendarDAO();

    // Async fetch all dates that have notes in a given month
    public static Task<Set<LocalDate>> getNoteDatesForMonthAsync(YearMonth yearMonth) {
        Task<Set<LocalDate>> task = new Task<>() {
            @Override
            protected Set<LocalDate> call() {
                return getNoteDatesForMonth(yearMonth);
            }
        };
        return task;
    }

    // Get all dates with notes for the month (blocking method)
    private static Set<LocalDate> getNoteDatesForMonth(YearMonth yearMonth) {
        Set<LocalDate> datesWithNotes = new HashSet<>();
        // Naive approach: check each day for note, optimize if needed
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            String note = calendarDAO.getNoteForDate(date);
            if (note != null && !note.isEmpty()) {
                datesWithNotes.add(date);
            }
        }
        return datesWithNotes;
    }

    // Show dialog to add or update note for a date
    public static void visaAnteckningsDialog(LocalDate datum) {
        String existingNote = calendarDAO.getNoteForDate(datum);
        TextInputDialog dialog = new TextInputDialog(existingNote != null ? existingNote : "");
        dialog.setTitle("Anteckning för " + datum);
        dialog.setHeaderText(null);
        dialog.setContentText("Skriv anteckning:");

        Optional<String> resultat = dialog.showAndWait();
        resultat.ifPresent(text -> {
            if (!text.trim().isEmpty()) {
                calendarDAO.saveOrUpdate(datum, text.trim());
            }
        });
    }

    // Show existing note in dialog (read-only)
    public static void visaBefintligAnteckning(LocalDate datum) {
        String note = calendarDAO.getNoteForDate(datum);
        if (note == null || note.isEmpty()) {
            System.out.println("Ingen anteckning för datumet: " + datum);
            return;
        }
        TextInputDialog dialog = new TextInputDialog(note);
        dialog.setTitle("Visa anteckning för " + datum);
        dialog.setHeaderText(null);
        dialog.setContentText("Anteckning:");
        // Disable editing by disabling text field
        dialog.getEditor().setEditable(false);
        dialog.showAndWait();
    }

    // Delete note for date
    public static void taBortAnteckning(LocalDate datum) {
        boolean success = calendarDAO.deleteNote(datum);
        if (!success) {
            System.err.println("Kunde inte ta bort anteckning för datum: " + datum);
        }
    }
}
