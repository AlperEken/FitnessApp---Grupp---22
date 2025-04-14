package org.FitnessApp1.model;

import java.time.LocalDate;

public class KaloriLogg {

    private int loggID; // unikt ID från databasen
    private LocalDate datum;
    private String beskrivning;
    private int kalorier;
    private int kontoID;

    public KaloriLogg(int loggID, LocalDate datum, String beskrivning, int kalorier, int kontoID) {
        this.loggID = loggID;
        this.datum = datum;
        this.beskrivning = beskrivning;
        this.kalorier = kalorier;
        this.kontoID = kontoID;
    }

    public KaloriLogg(LocalDate datum, String beskrivning, int kalorier, int kontoID) {
        this(-1, datum, beskrivning, kalorier, kontoID); // används vid ny post
    }

    // Getters & setters
    public int getLoggID() {
        return loggID;
    }

    public void setLoggID(int loggID) {
        this.loggID = loggID;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public int getKalorier() {
        return kalorier;
    }

    public void setKalorier(int kalorier) {
        this.kalorier = kalorier;
    }

    public int getKontoID() {
        return kontoID;
    }

    public void setKontoID(int kontoID) {
        this.kontoID = kontoID;
    }

    @Override
    public String toString() {
        return beskrivning + " (" + kalorier + " kcal)";
    }
}
