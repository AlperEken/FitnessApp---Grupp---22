package org.FitnessApp1.model;

public class Account {
    private int accountID;
    private String name;
    private String lastName;
    private String epost;
    private String password;
    private int age;
    private double weight;
    private String gender;
    private int dailyGoal;

    // Tom konstruktor
    public Account() {}

    // Konstruktor för nyregistrering (utan ID)
    public Account(String name, String lastName, String epost, String password,
                   int age, double weight, String gender, int dailyGoal) {
        this.name = name;
        this.lastName = lastName;
        this.epost = epost;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.dailyGoal = dailyGoal;
    }

    // Fullständig konstruktor inkl. kontoID
    public Account(int accountID, String name, String lastName, String epost, String password,
                   int age, double weight, String gender, int dailyGoal) {
        this.accountID = accountID;
        this.name = name;
        this.lastName = lastName;
        this.epost = epost;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.dailyGoal = dailyGoal;
    }

    // Getters & Setters
    public int getAccountID() { return accountID; }
    public void setAccountID(int kontoID) { this.accountID = kontoID; }

    public String getName() { return name; }
    public void setName(String namn) { this.name = name; }

    public String getLastname() { return lastName; }
    public void setLastname(String efternamn) { this.lastName = lastName; }

    public String getEmail() { return epost; }
    public void setEmail(String epost) { this.epost = epost; }

    public String getPassword() { return password; }
    public void setPassword(String lösenord) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int ålder) { this.age = age; }

    public double getWeight() { return weight; }
    public void setWeight(double vikt) { this.weight = weight; }

    public String getGender() { return gender; }
    public void setGender(String kön) { this.gender = gender; }

    public int getDaliyGoals() { return dailyGoal; }
    public void setDailyGoals(int dagligtMal) { this.dailyGoal = dailyGoal; }

    @Override
    public String toString() {
        return name + " " + lastName + " (" + epost + ")";
    }
}