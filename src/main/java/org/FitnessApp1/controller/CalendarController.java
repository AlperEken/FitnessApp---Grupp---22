package org.FitnessApp1.controller;

import javafx.scene.control.*;
import org.FitnessApp1.model.CalendarDAO;

import java.time.LocalDate;
import java.util.Optional;

public class CalendarController {

    private static CalendarDAO dao = new CalendarDAO();

    public static void visaAnteckningsDialog(LocalDate datum) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Anteckning");
        dialog.setHeaderText("Lägg till eller redigera anteckning för " + datum);
        dialog.setContentText("Anteckning:");

        // Hämta befintlig anteckning om den finns
        String befintlig = dao.getNoteForDate(datum);
        if (befintlig != null) {
            dialog.getEditor().setText(befintlig);
        }

        Optional<String> resultat = dialog.showAndWait();
        resultat.ifPresent(text -> {
            if (text.isBlank()) {
                dao.deleteNote(datum);
            } else {
                dao.saveOrUpdate(datum, text);
            }
        });
    }

    public static void taBortAnteckning(LocalDate datum) {
        boolean success = dao.deleteNote(datum);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ta bort anteckning");
        alert.setHeaderText(null);
        if (success) {
            alert.setContentText("Anteckningen för " + datum + " togs bort.");
        } else {
            alert.setContentText("Det fanns ingen anteckning att ta bort eller ett fel uppstod.");
        }
        alert.showAndWait();
    }


    public static void visaBefintligAnteckning(LocalDate datum) {
        String note = dao.getNoteForDate(datum);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Visa anteckning");
        alert.setHeaderText("Anteckning för " + datum);
        if (note != null && !note.isBlank()) {
            alert.setContentText(note);
        } else {
            alert.setContentText("Ingen anteckning finns för detta datum.");
        }
        alert.showAndWait();
    }

    public static boolean harAnteckning(LocalDate datum) {
        String note = dao.getNoteForDate(datum);
        return note != null && !note.isBlank();
    }
}
