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
    public int getAccountID() { return kontoID; }
    public void setAccountID(int kontoID) { this.kontoID = kontoID; }

    public String getName() { return namn; }
    public void setName(String namn) { this.namn = namn; }

    public String getLastname() { return efternamn; }
    public void setLastname(String efternamn) { this.efternamn = efternamn; }

    public String getEmail() { return epost; }
    public void setEmail(String epost) { this.epost = epost; }

    public String getPassword() { return lösenord; }
    public void setPassword(String lösenord) { this.lösenord = lösenord; }

    public int getAge() { return ålder; }
    public void setAge(int ålder) { this.ålder = ålder; }

    public double getWeight() { return vikt; }
    public void setWeight(double vikt) { this.vikt = vikt; }

    public String getGender() { return kön; }
    public void setGender(String kön) { this.kön = kön; }

    public int getDaliyGoals() { return dagligtMal; }
    public void setDailyGoals(int dagligtMal) { this.dagligtMal = dagligtMal; }

    @Override
    public String toString() {
        return namn + " " + efternamn + " (" + epost + ")";
    }
}