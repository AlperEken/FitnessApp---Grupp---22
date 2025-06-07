package org.FitnessApp1.model;

import java.time.LocalDate;

public class CalorieLog {

    private int loggID; // unikt ID från databasen
    private LocalDate datum;
    private String beskrivning;
    private int kalorier;
    private int kontoID;

    public CalorieLog(int loggID, LocalDate datum, String beskrivning, int kalorier, int kontoID) {
        this.loggID = loggID;
        this.datum = datum;
        this.beskrivning = beskrivning;
        this.kalorier = kalorier;
        this.kontoID = kontoID;
    }

    public CalorieLog(LocalDate datum, String beskrivning, int kalorier, int kontoID) {
        this(-1, datum, beskrivning, kalorier, kontoID); // används vid ny post
    }

    // Getters & setters
    public int getLogID() {
        return loggID;
    }

    public void setLogID(int loggID) {
        this.loggID = loggID;
    }

    public LocalDate getDate() {
        return datum;
    }

    public void setDate(LocalDate datum) {
        this.datum = datum;
    }

    public String getDescription() {
        return beskrivning;
    }

    public void setDescription(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public int getCalories() {
        return kalorier;
    }

    public void setCalories(int kalorier) {
        this.kalorier = kalorier;
    }

    public int getAccountID() {
        return kontoID;
    }

    public void setAccountID(int kontoID) {
        this.kontoID = kontoID;
    }

    @Override
    public String toString() {
        return beskrivning + " (" + kalorier + " kcal)";
    }
}
