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

    // Async fetch all dates that have notes in a given month for given kontoid
    public static Task<Set<LocalDate>> getNoteDatesForMonthAsync(YearMonth yearMonth, int kontoid) {
        Task<Set<LocalDate>> task = new Task<>() {
            @Override
            protected Set<LocalDate> call() {
                return getNoteDatesForMonth(yearMonth, kontoid);
            }
        };
        return task;
    }

    // Get all dates with notes for the month (blocking method) for given kontoid
    private static Set<LocalDate> getNoteDatesForMonth(YearMonth yearMonth, int kontoid) {
        Set<LocalDate> datesWithNotes = new HashSet<>();
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            String note = calendarDAO.getNoteForDate(date, kontoid);
            if (note != null && !note.isEmpty()) {
                datesWithNotes.add(date);
            }
        }
        return datesWithNotes;
    }

    // Show dialog to add or update note for a date with kontoid
    public static void visaAnteckningsDialog(LocalDate datum, int kontoid) {
        String existingNote = calendarDAO.getNoteForDate(datum, kontoid);
        TextInputDialog dialog = new TextInputDialog(existingNote != null ? existingNote : "");
        dialog.setTitle("Anteckning för " + datum);
        dialog.setHeaderText(null);
        dialog.setContentText("Skriv anteckning:");

        Optional<String> resultat = dialog.showAndWait();
        resultat.ifPresent(text -> {
            if (!text.trim().isEmpty()) {
                calendarDAO.saveOrUpdate(datum, kontoid, text.trim());
            }
        });
    }

    // Show existing note in dialog (read-only) with kontoid
    public static void visaBefintligAnteckning(LocalDate datum, int kontoid) {
        String note = calendarDAO.getNoteForDate(datum, kontoid);
        if (note == null || note.isEmpty()) {
            System.out.println("Ingen anteckning för datumet: " + datum);
            return;
        }
        TextInputDialog dialog = new TextInputDialog(note);
        dialog.setTitle("Visa anteckning för " + datum);
        dialog.setHeaderText(null);
        dialog.setContentText("Anteckning:");
        dialog.getEditor().setEditable(false);
        dialog.showAndWait();
    }

    // Delete note for date with kontoid
    public static void taBortAnteckning(LocalDate datum, int kontoid) {
        boolean success = calendarDAO.deleteNote(datum, kontoid);
        if (!success) {
            System.err.println("Kunde inte ta bort anteckning för datum: " + datum);
        }
    }
}