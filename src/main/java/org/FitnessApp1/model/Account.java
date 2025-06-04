package org.FitnessApp1.model;

public class Account {
    private int kontoID;
    private String namn;
    private String efternamn;
    private String epost;
    private String lösenord;
    private int ålder;
    private double vikt;
    private String kön;
    private int dagligtMal;

    // Tom konstruktor
    public Account() {}

    // Konstruktor för nyregistrering (utan ID)
    public Account(String namn, String efternamn, String epost, String lösenord,
                   int ålder, double vikt, String kön, int dagligtMal) {
        this.namn = namn;
        this.efternamn = efternamn;
        this.epost = epost;
        this.lösenord = lösenord;
        this.ålder = ålder;
        this.vikt = vikt;
        this.kön = kön;
        this.dagligtMal = dagligtMal;
    }

    // Fullständig konstruktor inkl. kontoID
    public Account(int kontoID, String namn, String efternamn, String epost, String lösenord,
                   int ålder, double vikt, String kön, int dagligtMal) {
        this.kontoID = kontoID;
        this.namn = namn;
        this.efternamn = efternamn;
        this.epost = epost;
        this.lösenord = lösenord;
        this.ålder = ålder;
        this.vikt = vikt;
        this.kön = kön;
        this.dagligtMal = dagligtMal;
    }

    // Getters & Setters
    public int getKontoID() { return kontoID; }
    public void setKontoID(int kontoID) { this.kontoID = kontoID; }

    public String getNamn() { return namn; }
    public void setNamn(String namn) { this.namn = namn; }

    public String getEfternamn() { return efternamn; }
    public void setEfternamn(String efternamn) { this.efternamn = efternamn; }

    public String getEpost() { return epost; }
    public void setEpost(String epost) { this.epost = epost; }

    public String getLösenord() { return lösenord; }
    public void setLösenord(String lösenord) { this.lösenord = lösenord; }

    public int getÅlder() { return ålder; }
    public void setÅlder(int ålder) { this.ålder = ålder; }

    public double getVikt() { return vikt; }
    public void setVikt(double vikt) { this.vikt = vikt; }

    public String getKön() { return kön; }
    public void setKön(String kön) { this.kön = kön; }

    public int getDagligtMal() { return dagligtMal; }
    public void setDagligtMal(int dagligtMal) { this.dagligtMal = dagligtMal; }

    @Override
    public String toString() {
        return namn + " " + efternamn + " (" + epost + ")";
    }
}